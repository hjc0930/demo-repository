import {
  Controller,
  Get,
  Headers,
  Inject,
  Query,
  Res,
  Session,
  UnauthorizedException,
} from '@nestjs/common';
import { CACHE_MANAGER } from '@nestjs/cache-manager';
import { Cache } from 'cache-manager';
import { Response } from 'express';
import { JwtService } from '@nestjs/jwt';

@Controller()
export class AppController {
  @Inject(CACHE_MANAGER)
  private cacheManager: Cache;

  @Inject(JwtService)
  private jwtService: JwtService;

  @Get('ttt')
  ttt(
    @Headers('authorization') authorization: string,
    @Res({ passthrough: true }) response: Response,
  ) {
    if (authorization) {
      try {
        const token = authorization.split(' ')[1];
        const data = this.jwtService.verify(token);

        const newToken = this.jwtService.sign({
          count: data.count + 1,
        });
        response.setHeader('token', newToken);
        return data.count + 1;
      } catch (e) {
        console.log(e);
        throw new UnauthorizedException();
      }
    } else {
      const newToken = this.jwtService.sign({
        count: 1,
      });

      response.setHeader('token', newToken);
      return 1;
    }
  }

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

  @Get('sss')
  sss(@Session() session) {
    console.log(session);
    session.count = session.count ? session.count + 1 : 1;
    return session.count;
  }
}
