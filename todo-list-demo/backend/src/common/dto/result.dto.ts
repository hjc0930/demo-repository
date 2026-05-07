import { ApiProperty } from '@nestjs/swagger';

export class Result<T> {
  @ApiProperty()
  code: number;

  @ApiProperty()
  message: string;

  @ApiProperty({ required: false })
  data?: T;
}

export class PageResult<T> {
  @ApiProperty()
  list: T[];

  @ApiProperty()
  total: number;

  @ApiProperty()
  page: number;

  @ApiProperty()
  pageSize: number;
}

export function success<T>(data: T): Result<T> {
  return { code: 200, message: 'success', data };
}

export function successNoData(): Result<void> {
  return { code: 200, message: 'success' };
}

export function error(code: number, message: string): Result<void> {
  return { code, message };
}
