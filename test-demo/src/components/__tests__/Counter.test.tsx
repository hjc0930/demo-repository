import { describe, it, expect, vi } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { Counter } from '../Counter'

describe('Counter 组件', () => {
  it('应该正确渲染初始值', () => {
    render(<Counter initialValue={5} />)
    expect(screen.getByText('5')).toBeInTheDocument()
  })

  it('点击增加按钮应该增加计数', async () => {
    const user = userEvent.setup()
    render(<Counter initialValue={0} />)
    const incrementButton = screen.getByText('+')

    await user.click(incrementButton)
    expect(screen.getByText('1')).toBeInTheDocument()

    await user.click(incrementButton)
    expect(screen.getByText('2')).toBeInTheDocument()
  })

  it('点击减少按钮应该减少计数', async () => {
    const user = userEvent.setup()
    render(<Counter initialValue={5} />)
    const decrementButton = screen.getByText('-')

    await user.click(decrementButton)
    expect(screen.getByText('4')).toBeInTheDocument()
  })

  it('点击重置按钮应该重置为初始值', async () => {
    const user = userEvent.setup()
    render(<Counter initialValue={10} />)
    const incrementButton = screen.getByText('+')
    const resetButton = screen.getByText('重置')

    await user.click(incrementButton)
    expect(screen.getByText('11')).toBeInTheDocument()

    await user.click(resetButton)
    await waitFor(() => {
      expect(screen.getByText('10')).toBeInTheDocument()
    })
  })

  it('应该遵守最大值限制', async () => {
    const user = userEvent.setup()
    render(<Counter initialValue={9} max={10} />)
    const incrementButton = screen.getByText('+')

    await user.click(incrementButton)
    await waitFor(() => {
      expect(screen.getByText('10')).toBeInTheDocument()
    })

    expect(incrementButton).toBeDisabled()
  })

  it('应该遵守最小值限制', async () => {
    const user = userEvent.setup()
    render(<Counter initialValue={1} min={0} />)
    const decrementButton = screen.getByText('-')

    await user.click(decrementButton)
    await waitFor(() => {
      expect(screen.getByText('0')).toBeInTheDocument()
    })

    expect(decrementButton).toBeDisabled()
  })

  it('值变化时应该调用回调函数', async () => {
    const user = userEvent.setup()
    const onValueChange = vi.fn()
    render(<Counter initialValue={0} onValueChange={onValueChange} />)

    const incrementButton = screen.getByText('+')
    await user.click(incrementButton)

    expect(onValueChange).toHaveBeenCalledWith(1)
    expect(onValueChange).toHaveBeenCalledTimes(1)
  })

  it('应该显示计数器标题', () => {
    render(<Counter />)
    expect(screen.getByText('计数器')).toBeInTheDocument()
  })
})
