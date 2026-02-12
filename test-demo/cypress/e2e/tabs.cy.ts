describe('标签页导航 E2E 测试', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('应该显示所有标签页', () => {
    const tabs = ['计数器组件', '用户表单', '用户列表', '待办事项']
    tabs.forEach(tab => {
      cy.contains('.ant-tabs-tab', tab).should('be.visible')
    })
  })

  it('应该能够切换标签页', () => {
    // 默认在计数器组件页面
    cy.contains('Counter 组件示例').should('be.visible')

    // 切换到用户表单
    cy.contains('用户表单').click()
    cy.contains('UserForm 组件示例').should('be.visible')

    // 切换到用户列表
    cy.contains('用户列表').click()
    cy.contains('UserList 组件示例').should('be.visible')

    // 切换到待办事项
    cy.contains('待办事项').click()
    cy.contains('TodoList 组件示例').should('be.visible')

    // 切回计数器
    cy.contains('计数器组件').click()
    cy.contains('Counter 组件示例').should('be.visible')
  })

  it('应该记住上次访问的标签页', () => {
    // 切换到用户表单
    cy.contains('用户表单').click()
    cy.contains('UserForm 组件示例').should('be.visible')

    // 刷新页面
    cy.reload()

    // 应该还在用户表单页面（如果实现了状态持久化）
    // 这里我们只是验证刷新后页面正常
    cy.contains('UserForm 组件示例').should('be.visible')
  })
})
