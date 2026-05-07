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
    private readonly todoRepo: Repository<Todo>,
  ) {}

  async list(query: QueryTodoDto): Promise<PageResult<Todo>> {
    const qb = this.todoRepo.createQueryBuilder('todo');

    if (query.status !== undefined) {
      qb.andWhere('todo.status = :status', { status: query.status });
    }

    const total = await qb.getCount();

    const list = await qb
      .orderBy('todo.created_at', 'DESC')
      .offset((query.page - 1) * query.pageSize)
      .limit(query.pageSize)
      .getMany();

    return { list, total, page: query.page, pageSize: query.pageSize };
  }

  async getById(id: number): Promise<Todo> {
    const todo = await this.todoRepo.findOne({ where: { id } });
    if (!todo) {
      throw new BusinessException(404, 'Todo不存在');
    }
    return todo;
  }

  async create(dto: CreateTodoDto): Promise<Todo> {
    const todo = this.todoRepo.create({
      title: dto.title,
      description: dto.description ?? '',
      status: dto.status ?? 0,
      priority: dto.priority ?? 1,
      dueDate: dto.dueDate ? new Date(dto.dueDate) : null,
    });

    try {
      return await this.todoRepo.save(todo);
    } catch {
      throw new BusinessException(500, '创建失败');
    }
  }

  async update(id: number, dto: UpdateTodoDto): Promise<Todo> {
    const todo = await this.getById(id);

    if (dto.title !== undefined && dto.title.trim() !== '') {
      todo.title = dto.title.trim();
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

    try {
      return await this.todoRepo.save(todo);
    } catch {
      throw new BusinessException(500, '更新失败');
    }
  }

  async delete(id: number): Promise<void> {
    await this.getById(id);
    try {
      await this.todoRepo.softDelete(id);
    } catch {
      throw new BusinessException(500, '删除失败');
    }
  }
}
