import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { TodoModule } from './todo/todo.module';
import { Todo } from './todo/todo.entity';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (config: ConfigService) => {
        const driver = config.get<string>('DB_DRIVER', 'sqlite');

        if (driver === 'mysql') {
          return {
            type: 'mysql',
            host: config.get<string>('DB_HOST', 'localhost'),
            port: config.get<number>('DB_PORT', 3306),
            username: config.get<string>('DB_USERNAME', 'root'),
            password: config.get<string>('DB_PASSWORD', 'root'),
            database: config.get<string>('DB_DATABASE', 'todo_list'),
            entities: [Todo],
            synchronize: true,
          };
        }

        // default: sqlite
        return {
          type: 'better-sqlite3',
          database: config.get<string>('DB_STORAGE', './data/todos.sqlite'),
          entities: [Todo],
          synchronize: true,
        };
      },
    }),
    TodoModule,
  ],
})
export class AppModule {}
