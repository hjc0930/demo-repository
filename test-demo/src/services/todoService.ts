import { request, requestWithPagination } from './api'
import type { Todo, PaginationParams } from '../types'

// 获取待办事项列表（分页）
export const getTodoList = (params: PaginationParams) =>
  requestWithPagination.get<Todo>('/todos', params)

// 获取单个待办事项
export const getTodo = (id: number) =>
  request.get<Todo>(`/todos/${id}`)

// 创建待办事项
export const createTodo = (data: Omit<Todo, 'id'>) =>
  request.post<Todo>('/todos', data)

// 更新待办事项
export const updateTodo = (id: number, data: Partial<Todo>) =>
  request.put<Todo>(`/todos/${id}`, data)

// 删除待办事项
export const deleteTodo = (id: number) =>
  request.delete<void>(`/todos/${id}`)

// 切换待办事项完成状态
export const toggleTodo = (id: number, completed: boolean) =>
  request.put<Todo>(`/todos/${id}`, { completed })
