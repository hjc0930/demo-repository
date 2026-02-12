describe('用户表单 E2E 测试', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.contains('用户表单').click()
  })

  it('应该显示用户表单', () => {
    cy.contains('UserForm 组件示例').should('be.visible')
    cy.contains('label', '用户名').should('be.visible')
    cy.contains('label', '邮箱').should('be.visible')
    cy.contains('label', '密码').should('be.visible')
    cy.contains('button', '注册用户').should('be.visible')
  })

  it('应该验证必填字段', () => {
    // 直接点击提交按钮
    cy.contains('button', '注册用户').click()

    // 应该显示验证错误
    cy.contains('请输入用户名').should('be.visible')
    cy.contains('请输入邮箱').should('be.visible')
    cy.contains('请输入密码').should('be.visible')
  })

  it('应该验证邮箱格式', () => {
    cy.get('input[type="text"]').first().clear().type('testuser')
    cy.get('input[type="text"]').eq(1).clear().type('invalid-email')
    cy.get('input[type="password"]').type('password123')

    cy.contains('button', '注册用户').click()

    cy.contains('请输入有效的邮箱地址').should('be.visible')
  })

  it('应该验证用户名长度', () => {
    cy.get('input[type="text"]').first().clear().type('ab')
    cy.get('input[type="text"]').eq(1).clear().type('test@example.com')
    cy.get('input[type="password"]').type('password123')

    cy.contains('button', '注册用户').click()

    cy.contains('用户名长度为3-20个字符').should('be.visible')
  })

  it('应该成功提交表单', () => {
    const timestamp = Date.now()

    cy.get('input[type="text"]').first().clear().type(`testuser${timestamp}`)
    cy.get('input[type="text"]').eq(1).clear().type(`test${timestamp}@example.com`)
    cy.get('input[type="password"]').type('password123')

    // 拦截 API 调用（如果需要验证）
    cy.contains('button', '注册用户').click()

    // 应该显示成功消息（如果实现了）
    // cy.contains('操作成功').should('be.visible')

    // 表单应该被重置
    cy.get('input[type="text"]').first().should('have.value', '')
  })
})
