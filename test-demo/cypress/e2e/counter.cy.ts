describe('计数器组件 E2E 测试', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('应该显示计数器页面', () => {
    cy.contains('计数器组件').click()
    cy.contains('Counter 组件示例').should('be.visible')
  })

  it('应该正确显示初始计数', () => {
    cy.contains('计数器组件').click()
    cy.contains('0').should('be.visible')
  })

  it('应该能够增加计数', () => {
    cy.contains('计数器组件').click()

    // 找到第一个计数器的 + 按钮
    cy.contains('0').parent().parent().contains('+').click()
    cy.contains('1').should('be.visible')

    cy.contains('+').click()
    cy.contains('2').should('be.visible')
  })

  it('应该能够减少计数', () => {
    cy.contains('计数器组件').click()

    // 点击增加几次
    cy.contains('0').parent().parent().contains('+').click()
    cy.contains('0').parent().parent().contains('+').click()
    cy.contains('2').should('be.visible')

    // 点击减少
    cy.contains('-').click()
    cy.contains('1').should('be.visible')
  })

  it('应该能够重置计数', () => {
    cy.contains('计数器组件').click()

    // 增加计数
    cy.contains('+').click()
    cy.contains('+').click()
    cy.contains('2').should('be.visible')

    // 重置
    cy.contains('重置').click()
    cy.contains('0').should('be.visible')
  })

  it('应该遵守最大值限制', () => {
    cy.contains('计数器组件').click()

    // 第二个计数器 max=100, initialValue=50
    cy.contains('50').parent().parent().within(() => {
      // 不断点击 + 直到达到最大值
      for (let i = 0; i < 60; i++) {
        cy.contains('+').click()
      }
      // 应该显示 100
      cy.contains('100').should('be.visible')
      // + 按钮应该被禁用
      cy.contains('+').should('be.disabled')
    })
  })
})
