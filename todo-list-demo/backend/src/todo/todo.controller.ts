import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Param,
  Body,
  Query,
  ParseIntPipe,
} from '@nestjs/common';
import { TodoService } from './todo.service';
import { CreateTodoDto } from './dto/create-todo.dto';
import { UpdateTodoDto } from './dto/update-todo.dto';
import { QueryTodoDto } from './dto/query-todo.dto';
import { Todo } from './todo.entity';
import { PageResult } from '../common/dto/result.dto';

@Controller('todos')
export class TodoController {
  constructor(private readonly todoService: TodoService) {}

  @Get()
  list(@Query() query: QueryTodoDto): Promise<PageResult<Todo>> {
    return this.todoService.list(query);
  }

  @Get(':id')
  getById(@Param('id', ParseIntPipe) id: number): Promise<Todo> {
    return this.todoService.getById(id);
  }

  @Post()
  create(@Body() dto: CreateTodoDto): Promise<Todo> {
    return this.todoService.create(dto);
  }

  @Put(':id')
  update(
    @Param('id', ParseIntPipe) id: number,
    @Body() dto: UpdateTodoDto,
  ): Promise<Todo> {
    return this.todoService.update(id, dto);
  }

  @Delete(':id')
  delete(@Param('id', ParseIntPipe) id: number): Promise<void> {
    return this.todoService.delete(id);
  }
}
