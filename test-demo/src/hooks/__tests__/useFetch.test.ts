import { describe, it, expect, vi, beforeEach, afterEach, afterAll } from 'vitest'
import { renderHook, waitFor, act } from '@testing-library/react'
import { server } from '../../mocks/server'
import { useFetch } from '../useFetch'
import { getUserList } from '../../services/userService'
import { http, HttpResponse } from 'msw'

describe('useFetch Hook', () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: 'error' })
  })

  afterEach(() => {
    server.resetHandlers()
  })

  afterAll(() => {
    server.close()
  })

  it('应该成功获取数据', async () => {
    const fetchFn = vi.fn().mockResolvedValue({ data: 'test data' })

    const { result } = renderHook(() => useFetch(fetchFn))

    expect(result.current.loading).toBe(true)

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
      expect(result.current.data).toEqual({ data: 'test data' })
      expect(result.current.error).toBeNull()
    })
  })

  it('应该处理错误情况', async () => {
    const fetchFn = vi.fn().mockRejectedValue(new Error('Fetch failed'))

    const { result } = renderHook(() => useFetch(fetchFn))

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
      expect(result.current.error).toBeInstanceOf(Error)
      expect(result.current.error?.message).toBe('Fetch failed')
    })
  })

  it('应该支持 initialData', () => {
    const initialData = { items: [] }
    const fetchFn = vi.fn().mockResolvedValue({ items: [1, 2, 3] })

    const { result } = renderHook(() =>
      useFetch(fetchFn, { initialData })
    )

    expect(result.current.data).toEqual(initialData)
  })

  it('成功时应该调用 onSuccess 回调', async () => {
    const onSuccess = vi.fn()
    const data = { success: true }
    const fetchFn = vi.fn().mockResolvedValue(data)

    renderHook(() => useFetch(fetchFn, { onSuccess }))

    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalledWith(data)
    })
  })

  it('失败时应该调用 onError 回调', async () => {
    const onError = vi.fn()
    const error = new Error('API Error')
    const fetchFn = vi.fn().mockRejectedValue(error)

    renderHook(() => useFetch(fetchFn, { onError }))

    await waitFor(() => {
      expect(onError).toHaveBeenCalledWith(error)
    })
  })

  it('enabled 为 false 时不应该执行请求', async () => {
    const fetchFn = vi.fn().mockResolvedValue({ data: 'test' })

    renderHook(() => useFetch(fetchFn, { enabled: false }))

    await waitFor(() => {
      expect(fetchFn).not.toHaveBeenCalled()
    })
  })

  it('refetch 应该能够重新获取数据', async () => {
    let callCount = 0
    const fetchFn = vi.fn().mockImplementation(async () => {
      callCount++
      return { data: `call ${callCount}` }
    })

    const { result } = renderHook(() => useFetch(fetchFn))

    await waitFor(() => {
      expect(result.current.data).toEqual({ data: 'call 1' })
    })

    act(() => {
      result.current.refetch()
    })

    await waitFor(() => {
      expect(result.current.data).toEqual({ data: 'call 2' })
      expect(fetchFn).toHaveBeenCalledTimes(2)
    })
  })

  it('应该支持 async fetch 函数', async () => {
    const { result } = renderHook(() =>
      useFetch(async () => {
        const response = await getUserList()
        return response
      })
    )

    await waitFor(() => {
      expect(result.current.loading).toBe(false)
      expect(result.current.data).toBeDefined()
    })
  })
})
