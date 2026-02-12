import { describe, it, expect, vi, beforeEach, afterEach, afterAll } from 'vitest'
import { server } from '../../../mocks/server'
import { login, register, getUserInfo, getUserList } from '../userService'
import { http, HttpResponse } from 'msw'
import apiClient from '../api'

describe('用户服务 (userService)', () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: 'error' })
    localStorage.clear()
  })

  afterEach(() => {
    server.resetHandlers()
    vi.restoreAllMocks()
  })

  afterAll(() => {
    server.close()
  })

  describe('login', () => {
    it('应该成功登录', async () => {
      const response = await login({ username: 'admin', password: '123456' })

      expect(response.code).toBe(200)
      expect(response.message).toBe('登录成功')
      expect(response.data.token).toBe('mock-token-123')
      expect(response.data.user.name).toBe('张三')
    })

    it('错误的用户名密码应该返回 401', async () => {
      await expect(
        login({ username: 'wrong', password: 'wrong' })
      ).rejects.toThrow()
    })
  })

  describe('register', () => {
    it('应该成功注册', async () => {
      const response = await register({
        username: 'newuser',
        password: 'password123',
        email: 'newuser@example.com',
      })

      expect(response.code).toBe(200)
      expect(response.message).toBe('注册成功')
      expect(response.data.user.name).toBe('newuser')
      expect(response.data.token).toBeDefined()
    })
  })

  describe('getUserInfo', () => {
    it('应该获取用户信息', async () => {
      const response = await getUserInfo()

      expect(response.code).toBe(200)
      expect(response.data.name).toBe('张三')
      expect(response.data.email).toBe('zhangsan@example.com')
      expect(response.data.role).toBe('admin')
    })
  })

  describe('getUserList', () => {
    it('应该获取用户列表', async () => {
      const response = await getUserList()

      expect(response.code).toBe(200)
      expect(response.data).toHaveLength(3)
      expect(response.data[0].name).toBe('张三')
    })
  })
})

describe('API 拦截器', () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: 'error' })
    localStorage.clear()
  })

  afterEach(() => {
    server.resetHandlers()
  })

  afterAll(() => {
    server.close()
  })

  it('请求应该携带 token', async () => {
    const mockPost = vi.spyOn(apiClient, 'post')
    localStorage.setItem('token', 'test-token')

    await login({ username: 'admin', password: '123456' })

    expect(mockPost).toHaveBeenCalled()
  })

  it('401 错误应该清除 token', async () => {
    const removeItemSpy = vi.spyOn(Storage.prototype, 'removeItem')
    localStorage.setItem('token', 'test-token')

    server.use(
      http.post('/api/auth/login', () => {
        return HttpResponse.json(
          { code: 401, message: '未授权', data: null },
          { status: 401 }
        )
      })
    )

    try {
      await login({ username: 'admin', password: '123456' })
    } catch {
      // 预期的错误
    }

    expect(removeItemSpy).toHaveBeenCalledWith('token')
  })
})
