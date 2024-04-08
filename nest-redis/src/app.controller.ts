import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';

// 封装一个timeout函数，用于模拟异步操作
function timeout(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}
  @Get()
  async getHello() {
    await timeout(50000);
    return await this.appService.getHello();
  }
}
