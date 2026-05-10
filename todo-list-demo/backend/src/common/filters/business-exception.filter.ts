import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpException,
  HttpStatus,
} from '@nestjs/common';
import { Result } from '../dto/result.dto';

export class BusinessException extends HttpException {
  constructor(code: number, message: string) {
    const result = Result.error(code, message);
    super(result, code);
  }
}

@Catch()
export class BusinessExceptionFilter implements ExceptionFilter {
  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse();

    if (exception instanceof BusinessException) {
      response.status(exception.getStatus()).json(exception.getResponse());
      return;
    }

    if (exception instanceof HttpException) {
      const status = exception.getStatus();
      const message = exception.message || 'Internal Server Error';
      response.status(status).json(Result.error(status, message));
      return;
    }

    const status = HttpStatus.INTERNAL_SERVER_ERROR;
    response.status(status).json(Result.error(status, 'Internal Server Error'));
  }
}
