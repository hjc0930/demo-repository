import { http, HttpResponse } from 'msw'
import type { User, Todo } from '../types'

const mockUsers: User[] = [
  { id: 1, name: '张三', email: 'zhangsan@example.com', role: 'admin', createdAt: '2024-01-01' },
  { id: 2, name: '李四', email: 'lisi@example.com', role: 'user', createdAt: '2024-01-02' },
  { id: 3, name: '王五', email: 'wangwu@example.com', role: 'user', createdAt: '2024-01-03' },
]

let mockTodos: Todo[] = [
  { id: 1, title: '学习 React', completed: false, userId: 1 },
  { id: 2, title: '学习 TypeScript', completed: true, userId: 1 },
  { id: 3, title: '学习 Vitest', completed: false, userId: 2 },
]

export const handlers = [
  // 用户登录
  http.post('/api/auth/login', async ({ request }) => {
    const body = await request.json() as { username: string; password: string }
    if (body.username === 'admin' && body.password === '123456') {
      return HttpResponse.json({
        code: 200,
        message: '登录成功',
        data: {
          token: 'mock-token-123',
          user: mockUsers[0],
        },
      })
    }
    return HttpResponse.json({ code: 401, message: '用户名或密码错误', data: null }, { status: 401 })
  }),

  // 用户注册
  http.post('/api/auth/register', async ({ request }) => {
    const body = await request.json() as { username: string; password: string; email: string }
    const newUser: User = {
      id: mockUsers.length + 1,
      name: body.username,
      email: body.email,
      role: 'user',
      createdAt: new Date().toISOString(),
    }
    mockUsers.push(newUser)
    return HttpResponse.json({
      code: 200,
      message: '注册成功',
      data: {
        token: 'mock-token-new',
        user: newUser,
      },
    })
  }),

  // 获取用户信息
  http.get('/api/user/info', () => {
    return HttpResponse.json({
      code: 200,
      message: '成功',
      data: mockUsers[0],
    })
  }),

  // 获取用户列表
  http.get('/api/user/list', () => {
    return HttpResponse.json({
      code: 200,
      message: '成功',
      data: mockUsers,
    })
  }),

  // 获取待办事项列表（分页）
  http.get('/api/todos', ({ request }) => {
    const url = new URL(request.url)
    const page = Number(url.searchParams.get('page') || '1')
    const pageSize = Number(url.searchParams.get('pageSize') || '10')
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const data = mockTodos.slice(start, end)
    return HttpResponse.json({
      data,
      total: mockTodos.length,
      page,
      pageSize,
    })
  }),

  // 获取单个待办事项
  http.get('/api/todos/:id', ({ params }) => {
    const todo = mockTodos.find(t => t.id === Number(params.id))
    if (!todo) {
      return HttpResponse.json({ code: 404, message: '待办事项不存在', data: null }, { status: 404 })
    }
    return HttpResponse.json({ code: 200, message: '成功', data: todo })
  }),

  // 创建待办事项
  http.post('/api/todos', async ({ request }) => {
    const body = await request.json() as Omit<Todo, 'id'>
    const newTodo: Todo = { ...body, id: mockTodos.length + 1 }
    mockTodos.push(newTodo)
    return HttpResponse.json({ code: 200, message: '创建成功', data: newTodo })
  }),

  // 更新待办事项
  http.put('/api/todos/:id', async ({ params, request }) => {
    const index = mockTodos.findIndex(t => t.id === Number(params.id))
    if (index === -1) {
      return HttpResponse.json({ code: 404, message: '待办事项不存在', data: null }, { status: 404 })
    }
    const body = await request.json() as Partial<Todo>
    mockTodos[index] = { ...mockTodos[index], ...body }
    return HttpResponse.json({ code: 200, message: '更新成功', data: mockTodos[index] })
  }),

  // 删除待办事项
  http.delete('/api/todos/:id', ({ params }) => {
    const index = mockTodos.findIndex(t => t.id === Number(params.id))
    if (index === -1) {
      return HttpResponse.json({ code: 404, message: '待办事项不存在', data: null }, { status: 404 })
    }
    mockTodos.splice(index, 1)
    return HttpResponse.json({ code: 200, message: '删除成功', data: null })
  }),
]
