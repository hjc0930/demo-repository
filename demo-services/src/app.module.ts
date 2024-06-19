import { Module } from '@nestjs/common';
import { UserModule } from './modules/user/user.module';
import { TypeOrmModule } from '@nestjs/typeorm';
import { StudentModule } from './modules/student/student.module';
import { Student } from './modules/student/entities/student.entity';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { RolesModule } from './modules/roles/roles.module';
import getConfigPath from './config';
import { AppController } from './app.controller';

@Module({
  controllers: [AppController],
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: [getConfigPath()],
    }),
    TypeOrmModule.forRootAsync({
      useFactory: (configService: ConfigService) => {
        return {
          host: configService.get('mysql_host'),
          port: configService.get('mysql_port'),
          username: configService.get('mysql_username'),
          password: configService.get('mysql_password'),
          database: configService.get('mysql_database'),
          type: 'mysql',
          synchronize: false,
          logging: true,
          entities: [Student],
          poolSize: 10,
          connectorPackage: 'mysql2',
        };
      },
      inject: [ConfigService],
    }),
    UserModule,
    StudentModule,
    RolesModule,
  ],
})
export class AppModule {}
