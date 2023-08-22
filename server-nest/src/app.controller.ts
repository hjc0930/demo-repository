import {
  Body,
  Controller,
  Get,
  Inject,
  Logger,
  Post,
  UploadedFile,
  UploadedFiles,
  UseInterceptors,
  ValidationPipe,
} from '@nestjs/common';
import { AppService } from './app.service';
import { Demo } from './app.dto';
import {
  FileFieldsInterceptor,
  FileInterceptor,
  FilesInterceptor,
} from '@nestjs/platform-express';

@Controller()
export class AppController {
  @Inject(AppService)
  private readonly appService: AppService;

  @Inject('userInfo')
  private readonly userInfo: { name: string; age: number };

  @Get()
  async getHello() {
    await new Promise((resolve) => setTimeout(resolve, 5000));
    return {
      a: 1,
      b: 2,
    };
  }

  @Post()
  async ooo(@Body(new ValidationPipe()) obj: Demo) {
    console.log(obj);
    return '123';
  }
}
