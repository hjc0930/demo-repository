import { defineConfig } from 'cypress'
import { devServer } from '@cypress/vite-dev-server'

export default defineConfig({
  e2e: {
    supportFile: 'cypress/support/e2e.ts',
    baseUrl: 'http://localhost:5173',
    setupNodeEvents(on, config) {
      devServer(on, config)

      // 实现浏览器启动时的 node 事件监听器
      on('before:browser:launch', (options, browser) => {
        if (browser.family === 'chromium' && browser.name !== 'electron') {
          // 为 Chrome 添加启动参数
          options.args.push('--disable-site-isolation-trials')
        }
      })
    },
    specPattern: 'cypress/e2e/**/*.cy.{js,jsx,ts,tsx}',
    video: false,
    screenshotOnRunFailure: true,
    viewportWidth: 1280,
    viewportHeight: 720,
    defaultCommandTimeout: 10000,
  },
  // Cypress 专注于 E2E 测试
  // 组件单元/集成测试由 Vitest + React Testing Library 负责
  // 因此不需要 component 配置
})
