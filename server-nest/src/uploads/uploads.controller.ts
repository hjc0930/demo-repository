import {
  Body,
  Controller,
  FileTypeValidator,
  HttpException,
  Logger,
  MaxFileSizeValidator,
  ParseFilePipe,
  Post,
  UploadedFile,
  UploadedFiles,
  UseInterceptors,
} from '@nestjs/common';
import {
  FileFieldsInterceptor,
  FileInterceptor,
  FilesInterceptor,
} from '@nestjs/platform-express';

@Controller('uploads')
export class UploadsController {
  // single file upload
  @Post('file')
  @UseInterceptors(
    FileInterceptor('file', {
      dest: 'uploads',
    }),
  )
  uploadFile(
    @UploadedFile(
      new ParseFilePipe({
        exceptionFactory: (err) => {
          throw new HttpException('xxx' + err, 400);
        },
        validators: [
          new MaxFileSizeValidator({ maxSize: 1000 }),
          new FileTypeValidator({ fileType: 'image/jpeg' }),
        ],
      }),
    )
    file: Express.Multer.File,
    @Body() body,
  ) {
    console.log('body', body);
    console.log('file', file);
  }

  // multiple file upload
  @Post('files')
  @UseInterceptors(
    FilesInterceptor('files', 3, {
      dest: 'uploads',
    }),
  )
  uploadFiles(@UploadedFiles() files: Express.Multer.File[], @Body() body) {
    Logger.debug(files);
    Logger.debug(body);
  }

  // upload multiple fields and files
  @Post('file-multipFields')
  @UseInterceptors(
    FileFieldsInterceptor(
      [
        {
          name: 'file1',
          maxCount: 3,
        },
        {
          name: 'file2',
          maxCount: 2,
        },
      ],
      {
        dest: 'uploads',
      },
    ),
  )
  uploadMultipFields(
    @UploadedFiles()
    files: {
      file1: Express.Multer.File[];
      file2: Express.Multer.File[];
    },
    @Body() body,
  ) {
    Logger.debug(files);
    Logger.debug(body);
  }
}
