import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  ParseIntPipe,
  BadRequestException,
} from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { TodoService } from './todo.service';
import { CreateTodoDto } from './dto/create-todo.dto';
import { UpdateTodoDto } from './dto/update-todo.dto';
import { QueryTodoDto } from './dto/query-todo.dto';
import { Todo } from './todo.entity';
import { PageResult } from '../common/dto/result.dto';

const idPipe = new ParseIntPipe({
  exceptionFactory: () => new BadRequestException('无效的ID'),
});

@ApiTags('Todo管理')
@Controller('todos')
export class TodoController {
  constructor(private readonly todoService: TodoService) {}

  @Get()
  @ApiOperation({
    summary: '获取Todo列表',
    description: '分页获取Todo列表，支持按状态筛选',
  })
  async list(@Query() query: QueryTodoDto): Promise<PageResult<Todo>> {
    return this.todoService.list(query);
  }

  @Get(':id')
  @ApiOperation({ summary: '获取Todo详情', description: '根据ID获取Todo详情' })
  async getById(@Param('id', idPipe) id: number): Promise<Todo> {
    return this.todoService.getById(id);
  }

  @Post()
  @ApiOperation({ summary: '创建Todo', description: '创建一个新的Todo' })
  async create(@Body() dto: CreateTodoDto): Promise<Todo> {
    return this.todoService.create(dto);
  }

  @Put(':id')
  @ApiOperation({ summary: '更新Todo', description: '部分更新Todo' })
  async update(
    @Param('id', idPipe) id: number,
    @Body() dto: UpdateTodoDto,
  ): Promise<Todo> {
    return this.todoService.update(id, dto);
  }

  @Delete(':id')
  @ApiOperation({ summary: '删除Todo', description: '软删除Todo' })
  async delete(@Param('id', idPipe) id: number) {
    await this.todoService.delete(id);
  }
}
