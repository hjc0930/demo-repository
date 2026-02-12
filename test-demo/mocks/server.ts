/**
 * MSW Node.js Server Setup
 *
 * 此文件用于在 Node.js 环境中设置 MSW (Mock Service Worker)
 *
 * 使用场景：
 * - 单元测试：在 Vitest/Jest 等 Node.js 测试框架中 Mock API 请求
 * - E2E 测试：在 Cypress/Playwright 的 Node.js 环境中使用
 * - SSR 测试：测试服务端渲染逻辑
 *
 * 集成方式：
 * 在 test-setup.ts 或测试配置文件中启动 server：
 *
 * ```tsx
 * import { server } from '../mocks/server'
 * import { beforeAll, afterEach, afterAll } from 'vitest'
 *
 * beforeAll(() => server.listen())
 * afterEach(() => server.resetHandlers())
 * afterAll(() => server.close())
 * ```
 *
 * 与 browser.ts 的区别：
 * - server.ts 使用 setupServer，运行在 Node.js 环境（JSDOM）
 * - browser.ts 使用 setupWorker，运行在浏览器环境（需要 Service Worker）
 *
 * @see https://mswjs.io/docs/api/setup-server
 */

import { setupServer } from 'msw/node'
import { handlers } from './handlers'

/**
 * MSW Server 实例
 *
 * 使用 setupServer 创建一个拦截 Node.js 环境（如 JSDOM）HTTP 请求的服务器
 * 通过传入 handlers 数组，定义需要拦截的 API 路由和响应逻辑
 *
 * 主要方法：
 * - server.listen(options): 启动服务器，开始拦截请求
 * - server.resetHandlers(): 重置所有 handlers 到初始状态
 * - server.close(): 关闭服务器，停止拦截请求
 * - server.use(...handlers): 临时覆盖或添加新的 handlers
 */
export const server = setupServer(...handlers)
