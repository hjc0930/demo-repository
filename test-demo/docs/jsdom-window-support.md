# jsdom 对 window 对象属性的支持详解

> 本文档详细介绍 jsdom 对 window 对象上各种属性和方法的支持情况，帮助你在编写测试时决定哪些需要手动 mock。

## 目录

- [快速参考](#快速参考)
- [完全支持的属性](#完全支持的属性)
- [部分支持的属性](#部分支持的属性)
- [不支持的属性（需要 mock）](#不支持的属性需要-mock)
- [常见场景示例](#常见场景示例)
- [测试配置最佳实践](#测试配置最佳实践)

---

## 快速参考

| 属性/方法 | 支持情况 | 是否需要 Mock |
|-----------|----------|--------------|
| `localStorage` | ✅ 完全支持 | ❌ 否 |
| `sessionStorage` | ✅ 完全支持 | ❌ 否 |
| `getComputedStyle` | ⚠️ 部分支持 | ❌ 否 |
| `matchMedia` | ❌ 不支持 | ✅ 是 |
| `ResizeObserver` | ❌ 不支持 | ✅ 是 |
| `IntersectionObserver` | ❌ 不支持 | ✅ 是 |
| `MutationObserver` | ❌ 不支持 | ✅ 是 |
| `scrollTo` | ⚠️ 支持但无效 | 可选 |
| `fetch` | ✅ 完全支持 | 通常用 MSW |
| `requestAnimationFrame` | ✅ 完全支持 | ❌ 否 |
| `URL.createObjectURL` | ❌ 不支持 | ✅ 是 |

---

## 完全支持的属性

jsdom 完整实现以下标准 Web API，**无需手动 mock**：

### 存储类

| API | 支持情况 | 注意事项 |
|-----|----------|----------|
| `localStorage` | ✅ 完整支持 | 数据存储在内存中，测试间隔离 |
| `sessionStorage` | ✅ 完整支持 | 数据存储在内存中 |

```tsx
// 直接使用，无需 mock
localStorage.setItem('token', 'xxx')
expect(localStorage.getItem('token')).toBe('xxx')

// 测试后自动清理
afterEach(() => {
  localStorage.clear()
})
```

### 定时器类

| API | 支持情况 |
|-----|----------|
| `setTimeout` | ✅ |
| `clearTimeout` | ✅ |
| `setInterval` | ✅ |
| `clearInterval` | ✅ |
| `requestAnimationFrame` | ✅ |
| `cancelAnimationFrame` | ✅ |

```tsx
// 直接使用
vi.useFakeTimers() // 可选：使用虚拟时间
setTimeout(fn, 1000)
vi.runAllTimers()
```

### DOM 操作类

| API | 支持情况 |
|-----|----------|
| `document` | ✅ 完整支持 |
| `addEventListener` | ✅ |
| `removeEventListener` | ✅ |
| `dispatchEvent` | ✅ |
| `focus` | ✅ |
| `blur` | ✅ |
| `getSelection` | ✅ |

### 网络类

| API | 支持情况 | 推荐做法 |
|-----|----------|----------|
| `fetch` | ✅ | 配合 MSW mock |
| `XMLHttpRequest` | ✅ | 配合 MSW mock |
| `WebSocket` | ⚠️ 部分 | 建议用 MSW |

```tsx
// 使用 MSW mock API 请求
import { server } from './mocks/server'

beforeAll(() => server.listen())
afterEach(() => server.resetHandlers())
afterAll(() => server.close())
```

### 导航类

| API | 支持情况 | 限制 |
|-----|----------|------|
| `history` | ✅ 支持 | 不会真正导航 |
| `location` | ✅ 可读写 | 不会真正跳转 |

```tsx
// 可以测试路由逻辑
window.location.href = '/new-path'
expect(window.location.pathname).toBe('/new-path')
```

---

## 部分支持的属性

这些 API jsdom 有实现，但功能有限：

### getComputedStyle

| 特性 | 支持情况 |
|------|----------|
| 内联样式 (`style="..."`) | ✅ 完全支持 |
| CSS 类名样式 | ❌ 不支持 |
| 伪类/伪元素 | ❌ 不支持 |
| CSS 继承 | ⚠️ 部分支持 |

```tsx
// ✅ 支持：内联样式
const div = document.createElement('div')
div.style.textDecoration = 'line-through'
window.getComputedStyle(div).textDecoration // 'line-through'

// ❌ 不支持：CSS 类名
// <style>.red { color: red; }</style>
// div.className = 'red'
// window.getComputedStyle(div).color // ''
```

**测试建议**：

```tsx
// 使用内联样式，getComputedStyle 可正常工作
<span style={{ textDecoration: 'line-through' }}>

// 测试断言
expect(element).toHaveStyle({ textDecoration: 'line-through' })
expect(element.style.textDecoration).toBe('line-through')
```

### scrollTo / scrollBy / scrollIntoView

| API | 支持情况 | 实际效果 |
|-----|----------|----------|
| `scrollTo` | ⚠️ 方法存在 | 不会真正滚动 |
| `scrollBy` | ⚠️ 方法存在 | 不会真正滚动 |
| `scrollIntoView` | ⚠️ 方法存在 | 不会真正滚动 |

```tsx
// 可选：mock 以验证调用
vi.spyOn(window, 'scrollTo').mockImplementation(() => {})

// 测试验证
expect(window.scrollTo).toHaveBeenCalledWith({ top: 0, behavior: 'smooth' })
```

---

## 不支持的属性（需要 Mock）

以下 API jsdom **不提供**，必须在 test-setup.ts 中手动 mock：

### 媒体查询

| API | 用途 | 常见使用场景 |
|-----|------|-------------|
| `window.matchMedia()` | 检测媒体查询 | 响应式设计、断点测试 |

```tsx
// test-setup.ts 中添加
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
  })),
})

// 测试中使用
test('移动端显示汉堡菜单', () => {
  // 模拟移动端尺寸
  ;(window.matchMedia as any).mockImplementation(() => ({
    matches: true,
    media: '(max-width: 768px)',
  }))

  render(<Navigation />)
  expect(screen.getByRole('button', { name: '菜单' })).toBeInTheDocument()
})
```

### 观察器 API

| API | 用途 | 常见使用场景 |
|-----|------|-------------|
| `ResizeObserver` | 监听元素尺寸变化 | Antd Table, List, Calendar |
| `IntersectionObserver` | 监听元素可见性 | 虚拟滚动、懒加载 |
| `MutationObserver` | 监听 DOM 结构变化 | Antd Form, Tooltip |

```tsx
// test-setup.ts 中添加
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

// 类似地 mock IntersectionObserver 和 MutationObserver
```

### URL 相关

| API | 用途 | 支持情况 |
|-----|------|----------|
| `URL.createObjectURL()` | 创建 Blob URL | ❌ 不支持 |
| `URL.revokeObjectURL()` | 释放 Blob URL | ❌ 不支持 |

```tsx
// test-setup.ts 中添加
Object.defineProperty(URL, 'createObjectURL', {
  writable: true,
  value: vi.fn(() => 'mock:blob:url'),
})

Object.defineProperty(URL, 'revokeObjectURL', {
  writable: true,
  value: vi.fn(),
})
```

### 动画相关

| API | 支持情况 |
|-----|----------|
| `element.animate()` | ❌ |
| `window.getAnimations()` | ❌ |
| `requestIdleCallback()` | ❌ |

---

## 常见场景示例

### 场景 1：测试 localStorage

```tsx
// ✅ 无需 mock，直接使用
test('保存用户 token', () => {
  const { setToken } = useAuth()

  setToken('user-token-123')

  expect(localStorage.getItem('token')).toBe('user-token-123')
})

// 每个测试后清理
afterEach(() => {
  localStorage.clear()
})
```

### 场景 2：测试响应式布局

```tsx
// ✅ 需要 mock matchMedia
test('移动端显示汉堡菜单', () => {
  // Mock 移动端尺寸
  ;(window.matchMedia as any).mockImplementation(() => ({
    matches: true,
    media: '(max-width: 768px)',
  }))

  render(<Header />)
  expect(screen.getByLabelText('菜单')).toBeInTheDocument()
})
```

### 场景 3：测试 Antd Table

```tsx
// ✅ 需要 mock ResizeObserver
// 在 test-setup.ts 中已添加

test('表格加载数据', async () => {
  render(<Table dataSource={data} columns={columns} />)

  await waitFor(() => {
    expect(screen.getByText('张三')).toBeInTheDocument()
  })
})
```

### 场景 4：测试文件上传

```tsx
// ✅ 需要 mock URL.createObjectURL
test('图片预览', () => {
  const file = new File([''], 'test.png', { type: 'image/png' })

  render(<ImageUploader />)
  const input = screen.getByLabelText('上传')

  fireEvent.change(input, { target: { files: [file] } })

  expect(URL.createObjectURL).toHaveBeenCalledWith(file)
})
```

### 场景 5：测试样式变化

```tsx
// ✅ 使用内联样式，无需 mock getComputedStyle
test('完成任务后显示删除线', async () => {
  const user = userEvent.setup()
  render(<TodoItem />)

  await user.click(screen.getByRole('checkbox'))

  const title = screen.getByText('学习 React')
  expect(title.style.textDecoration).toBe('line-through')
  // 或者使用 jest-dom matcher
  expect(title).toHaveStyle({ textDecoration: 'line-through' })
})
```

---

## 测试配置最佳实践

### test-setup.ts 推荐配置

```tsx
import '@testing-library/jest-dom'
import { cleanup } from '@testing-library/react'
import { afterEach, beforeAll, afterAll, vi } from 'vitest'
import { server } from './mocks/server'

// ============================================
// MSW 配置
// ============================================
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
afterEach(() => {
  cleanup()
  server.resetHandlers()
})
afterAll(() => server.close())

// ============================================
// 必须 Mock（jsdom 不支持）
// ============================================

// matchMedia - 响应式测试
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
  })),
})

// ResizeObserver - Antd Table/List
class ResizeObserverMock {
  observe = vi.fn()
  disconnect = vi.fn()
}
Object.defineProperty(window, 'ResizeObserver', {
  writable: true,
  value: ResizeObserverMock,
})

// IntersectionObserver - 虚拟滚动
class IntersectionObserverMock {
  observe = vi.fn()
  disconnect = vi.fn()
}
Object.defineProperty(window, 'IntersectionObserver', {
  writable: true,
  value: IntersectionObserverMock,
})

// MutationObserver - DOM 变化监听
class MutationObserverMock {
  observe = vi.fn()
  disconnect = vi.fn()
}
Object.defineProperty(window, 'MutationObserver', {
  writable: true,
  value: MutationObserverMock,
})

// ============================================
// 可选 Mock（用于验证调用）
// ============================================

// scrollTo - 验证滚动调用
vi.spyOn(window, 'scrollTo').mockImplementation(() => {})

// URL.createObjectURL - 文件上传测试
Object.defineProperty(URL, 'createObjectURL', {
  writable: true,
  value: vi.fn(() => 'mock:blob:url'),
})

// ============================================
// 不要 Mock（jsdom 原生支持）
// ============================================

// ❌ 不要 mock 这些：
// - localStorage / sessionStorage
// - getComputedStyle
// - setTimeout / setInterval
// - requestAnimationFrame
// - addEventListener / removeEventListener
```

### 决策流程图

```
需要在测试中使用某个 Window API？
│
├─ 检查 jsdom 是否支持
│  ├─ 支持（如 localStorage）→ 直接使用，无需 mock
│  └─ 不支持（如 matchMedia）→ 在 test-setup.ts 中添加 mock
│
├─ 是否需要验证调用？
│  ├─ 是 → 使用 vi.spyOn() mock
│  └─ 否 → 正常使用
│
└─ 测试完成后是否需要清理？
   └─ 是 → 在 afterEach 中清理（如 localStorage.clear()）
```

### 常见错误及解决

| 错误 | 原因 | 解决方案 |
|------|------|----------|
| `matchMedia is not a function` | 缺少 matchMedia mock | 添加 matchMedia mock |
| `ResizeObserver is not defined` | 缺少 ResizeObserver mock | 添加 ResizeObserver mock |
| `toHaveStyle() 失败` | CSS 类名样式不被支持 | 使用内联样式或检查 className |
| `localStorage 数据残留` | 测试间未清理 | afterEach 中调用 `localStorage.clear()` |
| `fetch 请求失败` | 未配置 MSW | 配置 MSW server |

---

## 参考资源

- [jsdom 官方文档](https://github.com/jsdom/jsdom)
- [Vitest 配置文档](https://vitest.dev/config/)
- [Testing Library 文档](https://testing-library.com/docs/react-testing-library/intro/)
- [MSW 文档](https://mswjs.io/)
