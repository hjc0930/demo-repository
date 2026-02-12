import { useState, useEffect, useCallback } from 'react'

interface UseFetchOptions<T> {
  initialData?: T
  enabled?: boolean
  onSuccess?: (data: T) => void
  onError?: (error: Error) => void
}

interface UseFetchReturn<T> {
  data: T | undefined
  loading: boolean
  error: Error | null
  refetch: () => void
}

export const useFetch = <T>(
  fetchFn: () => Promise<T>,
  options: UseFetchOptions<T> = {}
): UseFetchReturn<T> => {
  const { initialData, enabled = true, onSuccess, onError } = options

  const [data, setData] = useState<T | undefined>(initialData)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  const fetch = useCallback(async () => {
    if (!enabled) return

    try {
      setLoading(true)
      setError(null)
      const result = await fetchFn()
      setData(result)
      onSuccess?.(result)
    } catch (err) {
      const errorObj = err instanceof Error ? err : new Error('Fetch failed')
      setError(errorObj)
      onError?.(errorObj)
    } finally {
      setLoading(false)
    }
  }, [fetchFn, enabled, onSuccess, onError])

  useEffect(() => {
    fetch()
  }, [fetch])

  return {
    data,
    loading,
    error,
    refetch: fetch,
  }
}

export default useFetch
