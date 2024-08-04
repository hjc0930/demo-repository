import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { join } from 'path';
import type { NestExpressApplication } from '@nestjs/platform-express';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe({ transform: true }));
  app.enableCors();
  (app as NestExpressApplication).useStaticAssets(
    join(__dirname, '../uploads'),
    { prefix: '/uploads' },
  );

  await app.listen(3000);
}
bootstrap();
