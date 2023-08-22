import {
  HttpException,
  HttpStatus,
  Injectable,
  PipeTransform,
} from '@nestjs/common';

@Injectable()
export class FileSizeValidationPipePipe implements PipeTransform {
  transform(value: Express.Multer.File) {
    if (value.size > 10 * 1024) {
      throw new HttpException(
        'The file cannot be greater than 10kb',
        HttpStatus.BAD_REQUEST,
      );
    }
    return value;
  }
}
