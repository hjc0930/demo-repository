import { Test, TestingModule } from '@nestjs/testing';
import { INestApplication, ValidationPipe } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import * as request from 'supertest';
import { TodoModule } from '../src/todo/todo.module';
import { Todo } from '../src/todo/todo.entity';
import { TransformInterceptor } from '../src/common/interceptors/transform.interceptor';
import { BusinessExceptionFilter } from '../src/common/filters/business-exception.filter';

describe('Todo E2E', () => {
  let app: INestApplication;

  beforeAll(async () => {
    const moduleFixture: TestingModule = await Test.createTestingModule({
      imports: [
        TypeOrmModule.forRoot({
          type: 'better-sqlite3',
          database: ':memory:',
          entities: [Todo],
          synchronize: true,
          dropSchema: true,
        }),
        TodoModule,
      ],
    }).compile();

    app = moduleFixture.createNestApplication();

    app.setGlobalPrefix('api/v1');

    app.useGlobalPipes(
      new ValidationPipe({
        transform: true,
        whitelist: true,
        forbidNonWhitelisted: false,
      }),
    );
    app.useGlobalInterceptors(new TransformInterceptor());
    app.useGlobalFilters(new BusinessExceptionFilter());

    await app.init();
  });

  afterAll(async () => {
    await app.close();
  });

  describe('GET /api/v1/todos', () => {
    it('should return empty list', async () => {
      const res = await request(app.getHttpServer()).get('/api/v1/todos');

      expect(res.status).toBe(200);
      expect(res.body.code).toBe(200);
      expect(res.body.message).toBe('success');
      expect(res.body.data.list).toEqual([]);
      expect(res.body.data.total).toBe(0);
      expect(res.body.data.page).toBe(1);
      expect(res.body.data.pageSize).toBe(10);
    });

    it('should return paginated list after creating todos', async () => {
      // Create 3 todos
      for (let i = 1; i <= 3; i++) {
        await request(app.getHttpServer())
          .post('/api/v1/todos')
          .send({ title: `Todo ${i}` });
      }

      const res = await request(app.getHttpServer())
        .get('/api/v1/todos')
        .query({ page: 1, pageSize: 2 });

      expect(res.status).toBe(200);
      expect(res.body.data.list.length).toBe(2);
      expect(res.body.data.total).toBe(3);
      expect(res.body.data.page).toBe(1);
      expect(res.body.data.pageSize).toBe(2);
    });

    it('should filter by status', async () => {
      await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ title: 'Done Todo', status: 2 });

      const res = await request(app.getHttpServer())
        .get('/api/v1/todos')
        .query({ status: 2 });

      expect(res.status).toBe(200);
      expect(res.body.data.list.length).toBe(1);
      expect(res.body.data.list[0].title).toBe('Done Todo');
      expect(res.body.data.list[0].status).toBe(2);
    });
  });

  describe('GET /api/v1/todos/:id', () => {
    it('should return a single todo', async () => {
      const createRes = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ title: 'Single Todo' });

      const id = createRes.body.data.id;

      const res = await request(app.getHttpServer()).get(`/api/v1/todos/${id}`);

      expect(res.status).toBe(200);
      expect(res.body.code).toBe(200);
      expect(res.body.data.id).toBe(id);
      expect(res.body.data.title).toBe('Single Todo');
    });

    it('should return 404 for non-existent todo', async () => {
      const res = await request(app.getHttpServer()).get('/api/v1/todos/99999');

      expect(res.status).toBe(404);
      expect(res.body.code).toBe(404);
    });
  });

  describe('POST /api/v1/todos', () => {
    it('should create a todo with default values', async () => {
      const res = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ title: 'New Todo' });

      expect(res.status).toBe(201);
      expect(res.body.code).toBe(200);
      expect(res.body.data.title).toBe('New Todo');
      expect(res.body.data.status).toBe(0); // TODO by default
      expect(res.body.data.priority).toBe(1); // MEDIUM by default
      expect(res.body.data.description).toBe('');
      expect(res.body.data.dueDate).toBeNull();
    });

    it('should create a todo with all fields', async () => {
      const res = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({
          title: 'Full Todo',
          description: 'A description',
          status: 1,
          priority: 2,
          dueDate: '2025-12-31',
        });

      expect(res.status).toBe(201);
      expect(res.body.data.title).toBe('Full Todo');
      expect(res.body.data.description).toBe('A description');
      expect(res.body.data.status).toBe(1);
      expect(res.body.data.priority).toBe(2);
      expect(res.body.data.dueDate).toBeTruthy();
    });

    it('should reject without title', async () => {
      const res = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ description: 'No title' });

      expect(res.status).toBe(400);
    });
  });

  describe('PUT /api/v1/todos/:id', () => {
    it('should update a todo partially', async () => {
      const createRes = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ title: 'To Update' });

      const id = createRes.body.data.id;

      const res = await request(app.getHttpServer())
        .put(`/api/v1/todos/${id}`)
        .send({ title: 'Updated Title', status: 2 });

      expect(res.status).toBe(200);
      expect(res.body.data.title).toBe('Updated Title');
      expect(res.body.data.status).toBe(2);
    });

    it('should set dueDate to null', async () => {
      const createRes = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ title: 'With Date', dueDate: '2025-06-01' });

      const id = createRes.body.data.id;

      const res = await request(app.getHttpServer())
        .put(`/api/v1/todos/${id}`)
        .send({ dueDate: null });

      expect(res.status).toBe(200);
      expect(res.body.data.dueDate).toBeNull();
    });

    it('should return 404 for non-existent todo', async () => {
      const res = await request(app.getHttpServer())
        .put('/api/v1/todos/99999')
        .send({ title: 'Nope' });

      expect(res.status).toBe(404);
    });
  });

  describe('DELETE /api/v1/todos/:id', () => {
    it('should soft delete a todo', async () => {
      const createRes = await request(app.getHttpServer())
        .post('/api/v1/todos')
        .send({ title: 'To Delete' });

      const id = createRes.body.data.id;

      const res = await request(app.getHttpServer()).delete(`/api/v1/todos/${id}`);

      expect(res.status).toBe(200);
      expect(res.body.code).toBe(200);

      // Verify it's not in the list anymore
      const listRes = await request(app.getHttpServer()).get('/api/v1/todos');
      const deleted = listRes.body.data.list.find((t: Todo) => t.id === id);
      expect(deleted).toBeUndefined();
    });

    it('should return 404 for non-existent todo', async () => {
      const res = await request(app.getHttpServer()).delete('/api/v1/todos/99999');

      expect(res.status).toBe(404);
    });
  });
});
