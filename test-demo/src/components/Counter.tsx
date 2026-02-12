import { useState, useCallback } from 'react'
import { Button, Card, Space, Typography } from 'antd'

const { Title, Text } = Typography

interface CounterProps {
  initialValue?: number
  onValueChange?: (value: number) => void
  max?: number
  min?: number
}

export const Counter: React.FC<CounterProps> = ({
  initialValue = 0,
  onValueChange,
  max = 100,
  min = 0,
}) => {
  const [count, setCount] = useState(initialValue)

  const increment = useCallback(() => {
    setCount((prev) => {
      const newValue = Math.min(prev + 1, max)
      onValueChange?.(newValue)
      return newValue
    })
  }, [max, onValueChange])

  const decrement = useCallback(() => {
    setCount((prev) => {
      const newValue = Math.max(prev - 1, min)
      onValueChange?.(newValue)
      return newValue
    })
  }, [min, onValueChange])

  const reset = useCallback(() => {
    setCount(initialValue)
    onValueChange?.(initialValue)
  }, [initialValue, onValueChange])

  return (
    <Card style={{ width: 300, textAlign: 'center' }}>
      <Space orientation="vertical" size="middle">
        <Title level={3}>计数器</Title>
        <Text style={{ fontSize: 48, fontWeight: 'bold' }}>{count}</Text>
        <Space>
          <Button onClick={decrement} disabled={count <= min}>
            -
          </Button>
          <Button onClick={reset}>
            重置
          </Button>
          <Button onClick={increment} disabled={count >= max}>
            +
          </Button>
        </Space>
      </Space>
    </Card>
  )
}

export default Counter
