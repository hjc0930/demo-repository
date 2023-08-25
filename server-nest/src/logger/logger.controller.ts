import { Controller, Get, Inject, Logger } from '@nestjs/common';
import { LoggerService } from './logger.service';

@Controller('logger')
export class LoggerController {
  @Inject()
  private loggerService: LoggerService;

  private logger = new Logger();

  @Get()
  public getList() {
    this.logger.debug('123213123');
    return this.loggerService.getList();
  }
}
