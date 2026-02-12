// Cypress E2E 测试支持文件
import './commands'

// 全局配置
Cypress.config()

// 处理未捕获的异常
Cypress.on('uncaught:exception', (err) => {
  // 忽略 Ant Design 相关的警告
  if (err.message.includes('Warning:') || err.message.includes('Deprecated')) {
    return false
  }
  return true
})
