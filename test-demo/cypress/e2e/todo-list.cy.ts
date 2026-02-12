describe('待办事项 E2E 测试', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.contains('待办事项').click()
  })

  it('应该显示待办事项列表', () => {
    cy.contains('TodoList 组件示例').should('be.visible')
    cy.contains('学习 React').should('be.visible')
    cy.contains('学习 TypeScript').should('be.visible')
  })

  it('应该能够添加新的待办事项', () => {
    const newTodo = '新的待办事项 ' + Date.now()

    cy.get('input[placeholder*="输入新的待办事项"]').clear().type(newTodo)
    cy.contains('button', '添加').click()

    cy.contains(newTodo).should('be.visible')
  })

  it('空输入时添加应该显示警告', () => {
    // 拦截全局 alert/message
    cy.on('window:alert', (str) => {
      expect(str).to contain('请输入待办事项内容')
    })

    cy.contains('button', '添加').click()
  })

  it('应该能够切换待办事项完成状态', () => {
    // 找到 "学习 React" 对应的复选框并点击
    cy.contains('学习 React')
      .parents('.ant-list-item')
      .find('input[type="checkbox"]')
      .click()

    // 应该显示删除线样式
    cy.contains('学习 React').should('have.css', 'text-decoration', 'line-through')
  })

  it('应该能够删除待办事项', () => {
    const todoToDelete = '学习 React'

    // 找到待办事项并点击删除按钮
    cy.contains(todoToDelete)
      .parents('.ant-list-item')
      .find('.ant-btn-danger')
      .click()

    // 确认删除
    cy.contains('button', '确定').click()

    // 待办事项应该被删除
    cy.contains(todoToDelete).should('not.exist')
  })

  it('应该支持回车键添加待办事项', () => {
    const newTodo = '回车添加测试'

    cy.get('input[placeholder*="输入新的待办事项"]').clear().type(newTodo + '{enter}')

    cy.contains(newTodo).should('be.visible')
  })
})
