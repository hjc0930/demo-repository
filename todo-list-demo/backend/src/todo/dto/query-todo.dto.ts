import { IsOptional, IsInt, IsEnum, Min } from 'class-validator';
import { Type } from 'class-transformer';
import { TodoStatus } from '../enums/todo-status.enum';

export class QueryTodoDto {
  @IsOptional()
  @IsInt()
  @IsEnum(TodoStatus)
  @Type(() => Number)
  status?: TodoStatus;

  @IsOptional()
  @IsInt()
  @Min(1)
  @Type(() => Number)
  page?: number = 1;

  @IsOptional()
  @IsInt()
  @Min(1)
  @Type(() => Number)
  pageSize?: number = 10;
}
