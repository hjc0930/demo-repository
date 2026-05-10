import { IsString, IsOptional, IsInt, IsEnum, IsDateString } from 'class-validator';
import { TodoStatus } from '../enums/todo-status.enum';
import { Priority } from '../enums/priority.enum';

export class UpdateTodoDto {
  @IsOptional()
  @IsString()
  title?: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsOptional()
  @IsInt()
  @IsEnum(TodoStatus)
  status?: TodoStatus;

  @IsOptional()
  @IsInt()
  @IsEnum(Priority)
  priority?: Priority;

  @IsOptional()
  @IsDateString()
  dueDate?: string;
}
