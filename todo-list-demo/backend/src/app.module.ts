import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { TodoModule } from './todo/todo.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    TypeOrmModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (config: ConfigService) => {
        const driver = config.get<string>('DB_DRIVER', 'sqlite');

        if (driver === 'mysql') {
          return {
            type: 'mysql' as const,
            host: config.get<string>('DB_HOST', 'localhost'),
            port: config.get<number>('DB_PORT', 3306),
            username: config.get<string>('DB_USER', 'root'),
            password: config.get<string>('DB_PASSWORD', '123456'),
            database: config.get<string>('DB_NAME', 'todo_db'),
            autoLoadEntities: true,
            synchronize: true,
          };
        }

        return {
          type: 'better-sqlite3' as const,
          database: (config.get<string>('DB_NAME', 'todo_db')) + '.db',
          autoLoadEntities: true,
          synchronize: true,
        };
      },
    }),
    TodoModule,
  ],
})
export class AppModule {}
