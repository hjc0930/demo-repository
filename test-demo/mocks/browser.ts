/**
 * MSW Browser Worker Setup
 *
 * 此文件用于在浏览器环境中设置 MSW (Mock Service Worker)
 *
 * 使用场景：
 * - 开发环境：在前端开发时拦截 API 请求，返回模拟数据，无需依赖后端服务
 * - Storybook：在组件文档中展示组件时使用模拟数据
 * - 演示/原型：前端独立演示或原型开发
 *
 * 集成方式：
 * 在 src/main.tsx 或应用入口文件中启动 worker：
 *
 * ```tsx
 * import { worker } from '../mocks/browser'
 *
 * worker.start().then(() => {
 *   ReactDOM.createRoot(document.getElementById('root')!)
 *     .render(<App />)
 * })
 * ```
 *
 * 注意事项：
 * - setupWorker 仅在浏览器环境中可用
 * - 需要 Service Worker 支持（现代浏览器都支持）
 * - 首次启动时会注册 Service Worker，需确保 public 目录中有 mockServiceWorker.js
 *
 * @see https://mswjs.io/docs/api/setup-worker
 */

import { setupWorker } from 'msw/browser'
import { handlers } from './handlers'

/**
 * MSW Worker 实例
 *
 * 使用 setupWorker 创建一个 Service Worker 来拦截浏览器的网络请求
 * 通过传入 handlers 数组，定义需要拦截的 API 路由和响应逻辑
 */
export const worker = setupWorker(...handlers)
