import { useState, useCallback } from 'react'

interface UseCounterOptions {
  initialValue?: number
  max?: number
  min?: number
  step?: number
}

interface UseCounterReturn {
  count: number
  increment: () => void
  decrement: () => void
  reset: () => void
  setCount: (value: number | ((prev: number) => number)) => void
}

export const useCounter = (options: UseCounterOptions = {}): UseCounterReturn => {
  const { initialValue = 0, max = Infinity, min = -Infinity, step = 1 } = options

  const [count, setCountState] = useState(initialValue)

  const increment = useCallback(() => {
    setCountState((prev) => Math.min(prev + step, max))
  }, [max, step])

  const decrement = useCallback(() => {
    setCountState((prev) => Math.max(prev - step, min))
  }, [min, step])

  const reset = useCallback(() => {
    setCountState(initialValue)
  }, [initialValue])

  return {
    count,
    increment,
    decrement,
    reset,
    setCount: setCountState,
  }
}

export default useCounter
