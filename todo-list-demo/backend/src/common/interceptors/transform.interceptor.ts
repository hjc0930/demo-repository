import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { success, Result } from '../dto/result.dto';

@Injectable()
export class TransformInterceptor<T>
  implements NestInterceptor<T, Result<T>>
{
  intercept(
    context: ExecutionContext,
    next: CallHandler,
  ): Observable<Result<T>> {
    return next.handle().pipe(
      map((data) => {
        if (data === null || data === undefined) {
          return { code: 200, message: 'success' };
        }
        return success(data);
      }),
    );
  }
}
