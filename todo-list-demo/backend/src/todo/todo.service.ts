import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Todo } from './todo.entity';
import { CreateTodoDto } from './dto/create-todo.dto';
import { UpdateTodoDto } from './dto/update-todo.dto';
import { QueryTodoDto } from './dto/query-todo.dto';
import { PageResult } from '../common/dto/result.dto';
import { BusinessException } from '../common/filters/business-exception.filter';

@Injectable()
export class TodoService {
  constructor(
    @InjectRepository(Todo)
    private readonly todoRepository: Repository<Todo>,
  ) {}

  async list(query: QueryTodoDto): Promise<PageResult<Todo>> {
    const { status, page = 1, pageSize = 10 } = query;

    const where: Record<string, unknown> = {};
    if (status !== undefined) {
      where.status = status;
    }

    const [list, total] = await this.todoRepository.findAndCount({
      where,
      skip: (page - 1) * pageSize,
      take: pageSize,
      order: { createdAt: 'DESC' },
    });

    return new PageResult(list, total, page, pageSize);
  }

  async getById(id: number): Promise<Todo> {
    const todo = await this.todoRepository.findOne({ where: { id } });
    if (!todo) {
      throw new BusinessException(404, `Todo with id ${id} not found`);
    }
    return todo;
  }

  async create(dto: CreateTodoDto): Promise<Todo> {
    const todo = this.todoRepository.create({
      title: dto.title,
      description: dto.description || '',
      status: dto.status,
      priority: dto.priority,
      dueDate: dto.dueDate ? new Date(dto.dueDate) : null,
    });
    return this.todoRepository.save(todo);
  }

  async update(id: number, dto: UpdateTodoDto): Promise<Todo> {
    const todo = await this.getById(id);

    if (dto.title !== undefined) {
      todo.title = dto.title;
    }
    if (dto.description !== undefined) {
      todo.description = dto.description;
    }
    if (dto.status !== undefined) {
      todo.status = dto.status;
    }
    if (dto.priority !== undefined) {
      todo.priority = dto.priority;
    }
    if (dto.dueDate !== undefined) {
      todo.dueDate = dto.dueDate ? new Date(dto.dueDate) : null;
    }

    return this.todoRepository.save(todo);
  }

  async delete(id: number): Promise<void> {
    const result = await this.todoRepository.softDelete(id);
    if (result.affected === 0) {
      throw new BusinessException(404, `Todo with id ${id} not found`);
    }
  }
}
