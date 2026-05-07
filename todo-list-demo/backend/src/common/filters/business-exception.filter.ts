import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpException,
  HttpStatus,
} from '@nestjs/common';
import { Response } from 'express';
import { error } from '../dto/result.dto';

export class BusinessException extends Error {
  constructor(
    public readonly code: number,
    message: string,
  ) {
    super(message);
  }
}

@Catch()
export class BusinessExceptionFilter implements ExceptionFilter {
  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();

    if (exception instanceof BusinessException) {
      response.json(error(exception.code, exception.message));
      return;
    }

    if (exception instanceof HttpException) {
      const status = exception.getStatus();
      const exceptionResponse = exception.getResponse();

      let message = '服务器内部错误';
      if (typeof exceptionResponse === 'string') {
        message = exceptionResponse;
      } else if (
        typeof exceptionResponse === 'object' &&
        'message' in exceptionResponse
      ) {
        const messages = (exceptionResponse as { message: unknown }).message;
        if (Array.isArray(messages)) {
          message = this.translateValidationMessage(messages[0] as string);
        } else if (typeof messages === 'string') {
          message = messages;
        }
      }

      response.json(error(status, message));
      return;
    }

    response.json(error(500, '服务器内部错误'));
  }

  private translateValidationMessage(msg: string): string {
    if (msg.includes('must be shorter than or equal to')) {
      const match = msg.match(/must be shorter than or equal to (\d+)/);
      if (match) {
        if (msg.startsWith('title')) {
          return 'title长度不能超过' + match[1];
        }
        if (msg.startsWith('description')) {
          return 'description长度不能超过' + match[1];
        }
      }
    }
    if (msg.includes('must be longer than') || msg.includes('should not be empty')) {
      if (msg.startsWith('title')) {
        return 'title不能为空';
      }
    }
    if (msg.includes('must be a')) {
      return '参数校验失败';
    }
    return '参数校验失败';
  }
}
