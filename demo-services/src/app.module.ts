import { Module } from '@nestjs/common';
import { UserModule } from './modules/user/user.module';
import { TypeOrmModule } from '@nestjs/typeorm';
import { StudentModule } from './modules/student/student.module';
import { Student } from './modules/student/entities/student.entity';
import { ConfigModule } from '@nestjs/config';
import { RolesModule } from './modules/roles/roles.module';
import config from './config';

@Module({
  imports: [
    ConfigModule.forRoot({
      ignoreEnvFile: true,
      isGlobal: true,
      load: [config],
    }),
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: '192.168.124.5',
      port: 3306,
      username: 'root',
      password: '123456',
      database: 'demo',
      synchronize: false,
      logging: true,
      entities: [Student],
      poolSize: 10,
      connectorPackage: 'mysql2',
    }),
    UserModule,
    StudentModule,
    RolesModule,
  ],
})
export class AppModule {}
