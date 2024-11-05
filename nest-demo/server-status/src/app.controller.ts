import { Controller, Get, UseGuards } from '@nestjs/common';
import { AppService } from './app.service';
import * as os from 'node:os';
import * as nodeDiskInfo from 'node-disk-info';
import { LoginGuard } from './guards/login.guard';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get('bbb')
  @UseGuards(LoginGuard)
  bbb() {
    return 'bbb';
  }

  @Get('list')
  getList() {
    return [
      {
        id: 1,
        name: 'test-1',
        status: 'active',
      },
      {
        id: 2,
        name: 'test-2',
        status: 'active',
      },
      {
        id: 3,
        name: 'test-3',
        status: 'active',
      },
    ];
  }
  @Get()
  getHello(): string {
    return this.appService.getHello();
  }

  @Get('status')
  async getStatus() {
    return {
      cpu: this.getCpuInfo(),
      mem: this.getMemInfo(),
      dist: await this.getDiskStatus(),
      sys: this.getSysInfo(),
    };
  }

  private getSysInfo() {
    return {
      computerName: os.hostname(),
      computerIp: this.getServerIP(),
      osName: os.platform(),
      osArch: os.arch(),
    };
  }

  private getServerIP() {
    const nets = os.networkInterfaces();
    for (const name of Object.keys(nets)) {
      for (const net of nets[name]) {
        if (net.family === 'IPv4' && !net.internal) {
          return net.address;
        }
      }
    }
  }

  private async getDiskStatus() {
    const disks = await nodeDiskInfo.getDiskInfoSync();

    const sysFiles = disks.map((disk: any) => {
      return {
        dirName: disk._mounted,
        typeName: disk._filesystem,
        total: this.bytesToGB(disk._blocks) + 'GB',
        used: this.bytesToGB(disk._used) + 'GB',
        free: this.bytesToGB(disk._available) + 'GB',
        usage: ((disk._used / disk._blocks || 0) * 100).toFixed(2),
      };
    });
    return sysFiles;
  }

  private bytesToGB(bytes) {
    const gb = bytes / (1024 * 1024 * 1024);
    return gb.toFixed(2);
  }

  private getMemInfo() {
    const totalMemory = os.totalmem();
    const freeMemory = os.freemem();
    const usedMemory = totalMemory - freeMemory;
    const memoryUsagePercentage = (
      ((totalMemory - freeMemory) / totalMemory) *
      100
    ).toFixed(2);
    const mem = {
      total: this.bytesToGB(totalMemory),
      used: this.bytesToGB(usedMemory),
      free: this.bytesToGB(freeMemory),
      usage: memoryUsagePercentage,
    };
    return mem;
  }

  private getCpuInfo() {
    const cpus = os.cpus();
    const cpuInfo = cpus.reduce(
      (info, cpu) => {
        info.cpuNum += 1;
        info.user += cpu.times.user;
        info.sys += cpu.times.sys;
        info.idle += cpu.times.idle;
        info.total += cpu.times.user + cpu.times.sys + cpu.times.idle;
        return info;
      },
      { user: 0, sys: 0, idle: 0, total: 0, cpuNum: 0 },
    );
    const cpu = {
      cpuNum: cpuInfo.cpuNum,
      sys: ((cpuInfo.sys / cpuInfo.total) * 100).toFixed(2),
      used: ((cpuInfo.user / cpuInfo.total) * 100).toFixed(2),
      free: ((cpuInfo.idle / cpuInfo.total) * 100).toFixed(2),
    };
    return cpu;
  }
}
