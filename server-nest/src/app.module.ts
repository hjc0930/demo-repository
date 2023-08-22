import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UploadsModule } from './uploads/uploads.module';

@Module({
  imports: [UploadsModule],
  controllers: [AppController],
  providers: [
    AppService,
    {
      provide: 'userInfo',
      useValue: {
        name: 'abc',
        age: 18,
      },
    },
  ],
})
export class AppModule {}
