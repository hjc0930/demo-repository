import { Injectable } from '@nestjs/common';

@Injectable()
export class LoggerService {
  public getList() {
    return 'Hello List';
  }
}
