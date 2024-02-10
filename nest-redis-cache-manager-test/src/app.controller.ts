import { Controller, Get, Inject, Query } from '@nestjs/common';
import { CACHE_MANAGER } from '@nestjs/cache-manager';
import { Cache } from 'cache-manager';

@Controller()
export class AppController {
  @Inject(CACHE_MANAGER)
  private cacheManager: Cache;

  @Get()
  getHello() {
    return '';
  }

  @Get('set')
  async set(@Query('value') value: string) {
    await this.cacheManager.set('kkk', value);
    return 'done';
  }

  @Get('get')
  async get() {
    const result = await this.cacheManager.get('kkk');
    return result;
  }

  @Get('del')
  async del() {
    await this.cacheManager.del('kkk');
    return 'done';
  }
}
