describe('用户列表 E2E 测试', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.contains('用户列表').click()
  })

  it('应该显示用户列表表格', () => {
    cy.contains('UserList 组件示例').should('be.visible')
    cy.contains('用户列表').should('be.visible')

    // 验证表格列
    cy.contains('ID').should('be.visible')
    cy.contains('用户名').should('be.visible')
    cy.contains('邮箱').should('be.visible')
    cy.contains('角色').should('be.visible')
    cy.contains('操作').should('be.visible')
  })

  it('应该显示用户数据', () => {
    cy.contains('张三').should('be.visible')
    cy.contains('李四').should('be.visible')
    cy.contains('王五').should('be.visible')
  })

  it('管理员角色应该显示为管理员标签', () => {
    cy.contains('管理员').should('be.visible')
      .and('have.class', 'ant-tag')
  })

  it('普通用户角色应该显示为普通用户标签', () => {
    cy.contains('普通用户').should('be.visible')
      .and('have.class', 'ant-tag')
  })

  it('应该有编辑和删除按钮', () => {
    // 第一行应该有编辑和删除按钮
    cy.contains('tr', '张三').within(() => {
      cy.contains('button', '编辑').should('be.visible')
      cy.contains('button', '删除').should('be.visible')
    })
  })

  it('点击删除按钮应该显示确认对话框', () => {
    cy.contains('tr', '张三').contains('button', '删除').click()

    // 应该显示 Popconfirm
    cy.contains('确定要删除这个用户吗?').should('be.visible')
    cy.contains('button', '确定').should('be.visible')
    cy.contains('button', '取消').should('be.visible')

    // 取消删除
    cy.contains('button', '取消').click()
  })

  it('应该能够取消删除操作', () => {
    cy.contains('tr', '张三').contains('button', '删除').click()
    cy.contains('button', '取消').click()

    // 用户应该还在列表中
    cy.contains('张三').should('be.visible')
  })
})
