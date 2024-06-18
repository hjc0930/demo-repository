import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import getConfig from './config';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const config = getConfig();

  app.enableCors({
    origin: '*',
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE',
    allowedHeaders: 'Content-Type, Accept, Authorization',
  });

  await app.listen(parseInt(config.application.port, 10) || 3000);
}
bootstrap();
