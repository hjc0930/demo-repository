import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { JwtModule } from '@nestjs/jwt';

@Module({
  imports: [
    JwtModule.registerAsync({
      async useFactory() {
        return {
          secret: 'cheng123',
          signOptions: {
            expiresIn: '7d',
          },
        };
      },
    }),
    // JwtModule.register({
    //   secret: 'cheng123',
    //   signOptions: {
    //     expiresIn: '7d',
    //   },
    // }),
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
