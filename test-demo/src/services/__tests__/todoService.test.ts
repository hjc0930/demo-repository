import { describe, it, expect, beforeEach, afterEach, afterAll } from 'vitest'
import { server } from '../../../mocks/server'
import {
  getTodoList,
  getTodo,
  createTodo,
  updateTodo,
  deleteTodo,
  toggleTodo,
} from '../todoService'

describe('待办事项服务 (todoService)', () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: 'error' })
  })

  afterEach(() => {
    server.resetHandlers()
  })

  afterAll(() => {
    server.close()
  })

  describe('getTodoList', () => {
    it('应该获取待办事项列表', async () => {
      const response = await getTodoList({ page: 1, pageSize: 10 })

      expect(response.data).toHaveLength(3)
      expect(response.total).toBe(3)
      expect(response.page).toBe(1)
      expect(response.pageSize).toBe(10)
    })

    it('应该支持分页', async () => {
      const response = await getTodoList({ page: 1, pageSize: 2 })

      expect(response.data).toHaveLength(2)
      expect(response.total).toBe(3)
    })
  })

  describe('getTodo', () => {
    it('应该获取单个待办事项', async () => {
      const response = await getTodo(1)

      expect(response.code).toBe(200)
      expect(response.data.title).toBe('学习 React')
    })

    it('不存在的 ID 应该返回 404', async () => {
      await expect(getTodo(999)).rejects.toThrow()
    })
  })

  describe('createTodo', () => {
    it('应该创建新的待办事项', async () => {
      const newTodo = {
        title: '测试待办',
        completed: false,
        userId: 1,
      }

      const response = await createTodo(newTodo)

      expect(response.code).toBe(200)
      expect(response.data.title).toBe('测试待办')
      expect(response.data.id).toBeDefined()
    })
  })

  describe('updateTodo', () => {
    it('应该更新待办事项', async () => {
      const response = await updateTodo(1, { title: '更新后的标题' })

      expect(response.code).toBe(200)
      expect(response.data.title).toBe('更新后的标题')
    })

    it('应该支持部分更新', async () => {
      const response = await updateTodo(1, { completed: true })

      expect(response.code).toBe(200)
      expect(response.data.completed).toBe(true)
    })
  })

  describe('deleteTodo', () => {
    it('应该删除待办事项', async () => {
      const response = await deleteTodo(1)

      expect(response.code).toBe(200)
      expect(response.message).toBe('删除成功')
    })
  })

  describe('toggleTodo', () => {
    it('应该切换待办事项状态', async () => {
      const response = await toggleTodo(1, false)

      expect(response.code).toBe(200)
      expect(response.data.completed).toBe(true)
    })
  })
})
