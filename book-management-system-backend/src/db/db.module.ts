import { DynamicModule, Module } from '@nestjs/common';
import { DbService } from './db.service';

export interface DBModuleOptions {
  path: string;
}

@Module({})
export class DbModule {
  static register(options: DBModuleOptions): DynamicModule {
    return {
      module: DbModule,
      providers: [
        {
          provide: 'DB_OPTIONS',
          useValue: options,
        },
        DbService,
      ],
      exports: [DbService],
    };
  }
}
