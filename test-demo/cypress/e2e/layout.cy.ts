describe('页面布局 E2E 测试', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('应该显示页面头部', () => {
    cy.contains('测试演示项目').should('be.visible')
  })

  it('应该显示侧边栏', () => {
    cy.contains('h5', '技术栈').should('be.visible')

    const techStack = ['React', 'TypeScript', 'Vite', 'Ant Design', 'Axios', 'Vitest', 'MSW']
    techStack.forEach(tech => {
      cy.contains('li', tech).should('be.visible')
    })
  })

  it('应该显示运行测试命令', () => {
    cy.contains('h5', '运行测试').should('be.visible')
    cy.contains('npm test').should('be.visible')
  })

  it('侧边栏应该固定宽度', () => {
    cy.get('.ant-layout-sider').should('have.css', 'width', '250px')
  })

  it('主内容区域应该有正确背景色', () => {
    cy.get('.ant-layout-content').should('have.css', 'background-color', 'rgb(240, 242, 245)')
  })

  it('页面应该响应式布局', () => {
    // 测试不同视口大小
    cy.viewport(1920, 1080)
    cy.get('.ant-layout').should('be.visible')

    cy.viewport(768, 1024)
    cy.get('.ant-layout').should('be.visible')

    cy.viewport(375, 667)
    cy.get('.ant-layout').should('be.visible')
  })
})
