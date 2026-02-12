/// <reference types="cypress" />
/// <reference types="cypress-react" />

declare global {
  namespace Cypress {
    interface Chainable {
      mount: typeof import('cypress-react').mount
      login(username: string, password: string): void
      logout(): void
    }
  }
}

// 登录命令
Cypress.Commands.add('login', (username: string, password: string) => {
  cy.visit('/')
  cy.contains('button', '登录').click()
  cy.get('input[type="text"]').clear().type(username)
  cy.get('input[type="password"]').clear().type(password)
  cy.get('button[type="submit"]').click()
})

// 登出命令
Cypress.Commands.add('logout', () => {
  cy.get('.ant-dropdown').click()
  cy.contains('退出').click()
})
