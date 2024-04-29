import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { createClient } from 'redis';
import { UsersModule } from './users/users.module';
import { CityModule } from './city/city.module';
import { TypeOrmModule } from '@nestjs/typeorm';
import { City } from './city/entities/city.entity';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: 'localhost',
      port: 3306,
      username: 'root',
      password: '123456',
      database: 'demo',
      synchronize: true,
      logging: true,
      entities: [City],
      poolSize: 10,
      connectorPackage: 'mysql2',
      extra: {
        authPlugin: 'sha256_password',
      },
    }),
    UsersModule,
    CityModule,
  ],
  controllers: [AppController],
  providers: [
    AppService,
    {
      provide: 'REDIS_CLI',
      async useFactory() {
        const client = createClient({
          socket: {
            host: 'localhost',
            port: 6379,
          },
        });
        await client.connect();
        return client;
      },
    },
  ],
})
export class AppModule {}
