import '@testing-library/jest-dom'
import { cleanup } from '@testing-library/react'
import { afterEach, beforeAll, afterAll, vi } from 'vitest'
import { server } from './mocks/server'
import { resetMockData } from './mocks/handlers'

// ============================================================
// MSW (Mock Service Worker) 设置
// ============================================================

// 在所有测试开始前启动 MSW 服务器
beforeAll(() => {
  server.listen({
    onUnhandledRequest: 'error', // 对未处理的请求报错，帮助发现遗漏的 mock
  })
  console.log('MSW 服务器已启动')
})

afterEach(() => {
  // Testing Library 清理
  cleanup()
  // vi mock 清理
  vi.clearAllMocks();
  // 每个测试后重置 handlers，避免测试间相互影响
  server.resetHandlers();
  // 重置mock数据
  resetMockData()

})

// 所有测试结束后关闭 MSW 服务器
afterAll(() => {
  server.close()
  console.log('MSW 服务器已关闭')

})

// ============================================================
// jsdom 不支持的 Browser API - 需要手动 Mock
// ============================================================

/**
 * window.matchMedia Mock
 *
 * jsdom 不支持此 API
 * 用于响应式设计和媒体查询测试
 * Antd 组件（如 Menu, Drawer, Collapse）在特定断点下会使用
 *
 * @example 模拟移动端尺寸
 * (window.matchMedia as any).mockImplementation(() => ({ matches: true }))
 */
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query: string) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(), // Deprecated
    removeListener: vi.fn(), // Deprecated
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
})

/**
 * ResizeObserver Mock
 *
 * jsdom 不支持此 API
 * Antd 的很多组件（如 Table, List, Calendar）使用 ResizeObserver
 * 监听容器尺寸变化以进行布局调整
 */
class ResizeObserverMock {
  observe = vi.fn()
  unobserve = vi.fn()
  disconnect = vi.fn()
}

Object.defineProperty(window, 'ResizeObserver', {
  writable: true,
  configurable: true,
  value: ResizeObserverMock,
})

/**
 * IntersectionObserver Mock
 *
 * jsdom 不支持此 API
 * 用于检测元素是否进入视口
 * Antd 的 List 组件使用此 API 实现虚拟滚动
 */
class IntersectionObserverMock {
  root = null
  rootMargin = ''
  thresholds = []

  observe = vi.fn()
  unobserve = vi.fn()
  disconnect = vi.fn()
}

Object.defineProperty(window, 'IntersectionObserver', {
  writable: true,
  configurable: true,
  value: IntersectionObserverMock,
})

/**
 * MutationObserver Mock
 *
 * jsdom 不支持此 API
 * 用于监听 DOM 结构变化
 * Antd 部分组件（如 Form, Tooltip）会使用
 */
class MutationObserverMock {
  observe = vi.fn()
  disconnect = vi.fn()
  takeRecords = vi.fn(() => [])
}

Object.defineProperty(window, 'MutationObserver', {
  writable: true,
  configurable: true,
  value: MutationObserverMock,
})

// ============================================================
// 可选的 Mock（用于验证调用或简化测试）
// ============================================================

/**
 * window.scrollTo Mock
 *
 * jsdom 支持此方法但不会真正滚动
 * 使用 mock 可以在测试中验证是否被调用
 */
vi.spyOn(window, 'scrollTo').mockImplementation(() => { })

/**
 * URL.createObjectURL / revokeObjectURL Mock
 *
 * jsdom 不支持 Blob URL 创建
 * 主要用于文件上传、预览等功能测试
 */
Object.defineProperty(URL, 'createObjectURL', {
  writable: true,
  value: vi.fn(() => 'mock:blob:url'),
})

Object.defineProperty(URL, 'revokeObjectURL', {
  writable: true,
  value: vi.fn(),
})

// ============================================================
// jsdom 原生支持的 API - 无需 Mock
// ============================================================

/*
 * 以下 API jsdom 已原生支持，无需 mock：
 *
 * - localStorage / sessionStorage: 完整支持，数据存储在内存中
 * - getComputedStyle: 支持内联样式，不支持 CSS 类名样式
 * - setTimeout / setInterval / clearTimeout / clearInterval: 完整支持
 * - requestAnimationFrame / cancelAnimationFrame: 完整支持
 * - fetch / XMLHttpRequest: 完整支持（通常配合 MSW mock）
 * - history / location: 支持（但不能真正导航）
 * - addEventListener / removeEventListener: 完整支持
 * - focus / blur: 完整支持
 * - getSelection: 完整支持
 *
 * 注意：覆盖 jsdom 原生支持的 API 可能导致测试失败
 */
