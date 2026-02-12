describe('完整用户流程 E2E 测试', () => {
  it('用户完整操作流程', () => {
    cy.visit('/')

    // 1. 访问计数器页面并进行操作
    cy.contains('计数器组件').click()
    cy.contains('0').should('be.visible')

    cy.contains('+').click()
    cy.contains('1').should('be.visible')

    cy.contains('重置').click()
    cy.contains('0').should('be.visible')

    // 2. 访问用户表单并填写
    cy.contains('用户表单').click()

    const timestamp = Date.now()
    const username = `testuser${timestamp}`

    cy.get('input[type="text"]').first().clear().type(username)
    cy.get('input[type="text"]').eq(1).clear().type(`${username}@example.com`)
    cy.get('input[type="password"]').type('password123')

    cy.contains('button', '注册用户').click()

    // 等待表单重置
    cy.get('input[type="text"]').first().should('have.value', '')

    // 3. 访问用户列表查看数据
    cy.contains('用户列表').click()

    cy.contains('张三').should('be.visible')
    cy.contains('李四').should('be.visible')

    // 尝试删除一个用户
    cy.contains('tr', '李四').contains('button', '删除').click()
    cy.contains('button', '确定').click()

    // 4. 访问待办事项并操作
    cy.contains('待办事项').click()

    cy.contains('学习 React').should('be.visible')

    // 添加新待办
    const newTodo = `E2E 测试待办 ${timestamp}`
    cy.get('input[placeholder*="输入新的待办事项"]').type(newTodo)
    cy.contains('button', '添加').click()

    cy.contains(newTodo).should('be.visible')

    // 切换待办状态
    cy.contains('学习 React')
      .parents('.ant-list-item')
      .find('input[type="checkbox"]')
      .click()

    cy.contains('学习 React').should('have.css', 'text-decoration', 'line-through')

    // 5. 切换回计数器验证状态保持
    cy.contains('计数器组件').click()
    cy.contains('Counter 组件示例').should('be.visible')
  })

  it('键盘导航流程', () => {
    cy.visit('/')

    // 使用 Tab 键导航
    cy.get('body').tab()

    // 第一个 tab 应该获得焦点
    cy.focused().should('contain', '计数器组件')

    // 继续按 Tab 切换
    cy.get('body').tab()
    cy.focused().should('contain', '用户表单')

    cy.get('body').tab()
    cy.focused().should('contain', '用户列表')
  })

  it('页面刷新后状态恢复', () => {
    cy.visit('/')

    // 访问待办事项
    cy.contains('待办事项').click()
    cy.contains('TodoList 组件示例').should('be.visible')

    // 刷新页面
    cy.reload()

    // 应该能正常显示
    cy.contains('TodoList 组件示例').should('be.visible')
    cy.contains('学习 React').should('be.visible')
  })
})
