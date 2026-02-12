import { describe, it, expect } from 'vitest'
import { renderHook, act } from '@testing-library/react'
import { useCounter } from '../useCounter'

describe('useCounter Hook', () => {
  it('应该使用初始值初始化', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 5 }))
    expect(result.current.count).toBe(5)
  })

  it('默认初始值应该为 0', () => {
    const { result } = renderHook(() => useCounter())
    expect(result.current.count).toBe(0)
  })

  it('increment 应该增加计数值', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 0 }))

    act(() => {
      result.current.increment()
    })

    expect(result.current.count).toBe(1)
  })

  it('decrement 应该减少计数值', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 5 }))

    act(() => {
      result.current.decrement()
    })

    expect(result.current.count).toBe(4)
  })

  it('reset 应该重置为初始值', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 10 }))

    act(() => {
      result.current.increment()
      result.current.increment()
    })

    expect(result.current.count).toBe(12)

    act(() => {
      result.current.reset()
    })

    expect(result.current.count).toBe(10)
  })

  it('应该遵守最大值限制', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 9, max: 10 }))

    act(() => {
      result.current.increment()
    })
    expect(result.current.count).toBe(10)

    act(() => {
      result.current.increment()
    })
    expect(result.current.count).toBe(10)
  })

  it('应该遵守最小值限制', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 1, min: 0 }))

    act(() => {
      result.current.decrement()
    })
    expect(result.current.count).toBe(0)

    act(() => {
      result.current.decrement()
    })
    expect(result.current.count).toBe(0)
  })

  it('应该支持自定义步长', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 0, step: 5 }))

    act(() => {
      result.current.increment()
    })
    expect(result.current.count).toBe(5)

    act(() => {
      result.current.decrement()
    })
    expect(result.current.count).toBe(0)
  })

  it('setCount 应该能够直接设置值', () => {
    const { result } = renderHook(() => useCounter())

    act(() => {
      result.current.setCount(42)
    })

    expect(result.current.count).toBe(42)
  })

  it('setCount 应该支持函数式更新', () => {
    const { result } = renderHook(() => useCounter({ initialValue: 10 }))

    act(() => {
      result.current.setCount((prev) => prev * 2)
    })

    expect(result.current.count).toBe(20)
  })

  it('最大值应该为 Infinity（无限制）', () => {
    const { result } = renderHook(() => useCounter())

    act(() => {
      for (let i = 0; i < 1000; i++) {
        result.current.increment()
      }
    })

    expect(result.current.count).toBe(1000)
  })
})
