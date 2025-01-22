import { Body, Controller, Get, Post, Req } from '@nestjs/common';
import * as rawBody from 'raw-body';

@Controller()
export class AppController {
  @Get('ping')
  getHello(): string {
    return "It's work!";
  }
  @Post('body')
  async index(
    @Body() data: string,
    @Req() req: import('express').Request,
  ): Promise<string> {
    if (!req.readable) {
      return data;
    }
    const body = (await rawBody(req)).toString('utf8').trim();
    return body;
  }
}
