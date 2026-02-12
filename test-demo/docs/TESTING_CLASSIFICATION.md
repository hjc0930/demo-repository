# 测试分类规则

本文档定义了项目中测试的准确分类标准。

## 分类标准

测试分类的核心原则：**是否有外部数据/API 依赖**

## 分类层级

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         E2E 测试                                 │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │                    组件集成测试                              │  │
│  │  ┌────────────────────────────────────────────────────────────┐  │  │
│  │  │              组件单元测试                              │  │  │
│  │  │  ┌───────────────────────────────────────────────────┐   │  │  │
│  │  │  │           纯单元测试                           │   │  │  │
│  │  │  └───────────────────────────────────────────────────┘   │  │  │
│  │  └────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────┘
```

## 1. 纯单元测试

**定义**：测试独立的函数/逻辑，不涉及 UI 框架或外部依赖。

### 特征
- ✅ 纯 JavaScript/TypeScript 函数
- ✅ 无 React 组件
- ✅ 无外部 API 调用
- ✅ 无第三方库依赖
- ✅ 可直接调用函数并断言结果

### 项目中的示例

| 测试文件 | 测试对象 | 外部依赖 |
|----------|----------|----------|
| `src/utils/__tests__/format.test.ts` | 格式化函数 | 无 |
| `src/utils/__tests__/validation.test.ts` | 验证函数 | 无 |
| `src/hooks/__tests__/useCounter.test.ts` | Hook 逻辑 | 无 |

### 代码示例

```typescript
// ✅ 纯单元测试
describe('formatDate', () => {
  it('应该格式化日期', () => {
    const result = formatDate(new Date('2024-01-15'), 'YYYY-MM-DD')
    expect(result).toBe('2024-01-15')
  })
})
```

---

## 2. 组件单元测试

**定义**：测试 React 组件自身的状态和交互逻辑，即使使用了第三方 UI 库。

### 特征
- ✅ 测试 React 组件
- ✅ 可能使用第三方 UI 库（Ant Design、Material-UI 等）
- ✅ props 中的回调函数被 mock
- ✅ 无真实的 API 调用（或 API 已被 mock）
- ✅ 测试重点是组件自身的行为，而非 UI 库

### 判断标准
```typescript
// ✅ 组件单元测试 - onSubmit 是外部传入的 mock
const handleSubmit = vi.fn().mockResolvedValue(undefined)
render(<UserForm onSubmit={handleSubmit} />)

// ❌ 组件集成测试 - 组件内部调用真实 API
const { result } = renderHook(() => useUserList())
// 内部调用了 getUserList() API
```

### 项目中的示例

| 测试文件 | 测试对象 | UI 库 | API 调用 |
|----------|----------|--------|----------|
| `src/components/__tests__/Counter.test.tsx` | Counter 组件 | Ant Design | 无 |
| `src/components/__tests__/UserForm.test.tsx` | UserForm 组件 | Ant Design | 无（mock）|

### 代码示例

```typescript
// ✅ 组件单元测试
describe('Counter 组件', () => {
  it('应该能够增加计数', () => {
    const onValueChange = vi.fn()
    render(<Counter initialValue={0} onValueChange={onValueChange} />)

    fireEvent.click(screen.getByText('+'))

    expect(screen.getByText('1')).toBeInTheDocument()
    expect(onValueChange).toHaveBeenCalledWith(1)
  })
})
```

**关键**：Ant Design 只是渲染载体，不是测试目标。测试的是 Counter 自身的计数逻辑。

---

## 3. 组件集成测试

**定义**：测试组件与外部服务/API 的集成。

### 特征
- ✅ 测试 React 组件
- ✅ 组件内部调用外部 API 服务
- ✅ 使用 MSW mock HTTP 请求
- ✅ 测试包含数据获取、状态更新等异步流程

### 判断标准
```typescript
// ❌ 组件集成测试 - 内部调用真实 API
const UserList = () => {
  useEffect(() => {
    getUserList().then(setUsers)  // 调用 API
  }, [])
}
```

### 项目中的示例

| 测试文件 | 测试对象 | API 调用 |
|----------|----------|----------|
| `src/components/__tests__/UserList.test.tsx` | UserList 组件 | getUserList() |
| `src/components/__tests__/TodoList.test.tsx` | TodoList 组件 | getTodoList(), createTodo() 等 |
| `src/hooks/__tests__/useFetch.test.ts` | useFetch Hook | fetchFn (API 调用) |

### 代码示例

```typescript
// ✅ 组件集成测试
describe('UserList 组件', () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: 'error' })
  })

  it('应该正确渲染用户列表', async () => {
    render(<UserList onEdit={vi.fn()} />)

    // 等待 API 调用完成
    await waitFor(() => {
      expect(screen.getByText('张三')).toBeInTheDocument()
    })
  })
})
```

---

## 4. 服务集成测试

**定义**：测试 API 服务层与网络层的集成。

### 特征
- ✅ 测试 API 服务函数
- ✅ 使用 MSW mock HTTP 响应
- ✅ 测试各种 HTTP 场景（成功、失败、超时等）
- ✅ 测试拦截器逻辑

### 项目中的示例

| 测试文件 | 测试对象 | Mock 方式 |
|----------|----------|----------|
| `src/services/__tests__/userService.test.ts` | 用户服务 | MSW |
| `src/services/__tests__/todoService.test.ts` | 待办服务 | MSW |

### 代码示例

```typescript
// ✅ 服务集成测试
describe('用户服务', () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: 'error' })
  })

  it('应该成功登录', async () => {
    const response = await login({ username: 'admin', password: '123456' })
    expect(response.code).toBe(200)
  })
})
```

---

## 5. E2E 测试 (端到端测试)

**定义**：模拟真实用户在浏览器中的完整操作流程。

### 特征
- ✅ 使用真实浏览器（Chrome、Firefox 等）
- ✅ 测试完整用户流程
- ✅ 可能涉及多个页面和导航
- ✅ 不 mock 组件或服务
- ✅ 关注用户视角的行为

### 项目中的示例

| 测试文件 | 测试场景 | 运行环境 |
|----------|----------|----------|
| `cypress/e2e/counter.cy.ts` | 计数器页面操作 | 真实浏览器 |
| `cypress/e2e/user-form.cy.ts` | 用户表单填写提交 | 真实浏览器 |
| `cypress/e2e/workflow.cy.ts` | 完整用户流程 | 真实浏览器 |

### 代码示例

```typescript
// ✅ E2E 测试
describe('用户操作流程', () => {
  it('应该能够完成注册流程', () => {
    cy.visit('/')

    cy.contains('注册').click()
    cy.get('input[name="username"]').type('testuser')
    cy.get('input[name="email"]').type('test@example.com')
    cy.contains('提交').click()

    cy.contains('注册成功').should('be.visible')
  })
})
```

---

## Cypress Component 测试

Cypress Component 测试**介于组件单元测试和 E2E 测试之间**：

| 特征 | 说明 |
|------|------|
| 运行环境 | 真实浏览器（类似 E2E） |
| 测试范围 | 单个组件（类似单元测试） |
| 速度 | 比 E2E 快，比 Vitest 慢 |
| 使用场景 | 需要真实浏览器 API 的组件测试 |

### 项目中的示例

| 测试文件 | 说明 |
|----------|------|
| `cypress/component/Counter.cy.tsx` | 在真实浏览器中测试 Counter 组件 |
| `cypress/component/UserForm.cy.tsx` | 在真实浏览器中测试 UserForm 组件 |

---

## 快速判断流程图

```
开始
  │
  ├─ 是否测试纯函数逻辑？
  │   是 → 纯单元测试
  │   否 ↓
  │
  ├─ 是否测试 React 组件？
  │   否 → 服务集成测试
  │   是 ↓
  │
  ├─ 组件内是否调用外部 API？
  │   是 → 组件集成测试
  │   否 ↓
  │
  ├─ 是否需要完整浏览器环境？
  │   是 → Cypress Component 测试
  │   否 ↓
  │
  └─ 使用第三方 UI 库？
        │
        否 → 组件单元测试
        是 → 仍是组件单元测试（UI 库是载体，非测试目标）
```

---

## 关键原则

### 1. 依赖第三方 UI 库 ≠ 集成测试

```typescript
// ❌ 错误理解
"用了 Ant Design 就是集成测试"

// ✅ 正确理解
"测试的是组件自身的逻辑，Ant Design 只是渲染载体"
```

### 2. Mock 回调函数 ≠ 集成测试

```typescript
// onSubmit 被 mock，测试的是 UserForm 自身的表单验证
<UserForm onSubmit={vi.fn()} />

// 这是组件单元测试，不是集成测试
```

### 3. API 调用才是集成的标志

```typescript
// ❌ 单元测试
const handleSubmit = vi.fn()
<UserForm onSubmit={handleSubmit} />

// ✅ 集成测试
const UserList = () => {
  useEffect(() => {
    getUserList().then(setUsers)  // 真实 API 调用
  }, [])
}
```

---

## 测试金字塔应用

```
                  /\
                 /  E2E (少量)
               /        验证关键业务流程
             /  Cypress (真实浏览器)
           /
        /  组件集成测试 (适量)
      /      验证组件 + API 集成
    /
  /  组件单元测试 (大量)
│    验证组件自身逻辑
│
/  纯单元测试 (最多)
    验证独立函数逻辑
```

---

## 总结

| 测试类型 | 框架 | 数量 | 速度 | 覆盖目标 |
|----------|--------|------|------|----------|
| 纯单元测试 | Vitest | 最多 | 最快 | 纯函数逻辑 |
| 组件单元测试 | Vitest + RTL | 较多 | 快 | 组件状态和交互 |
| 组件集成测试 | Vitest + RTL + MSW | 适量 | 中等 | 组件 + API 集成 |
| 服务集成测试 | Vitest + MSW | 较少 | 中等 | API 层 |
| E2E 测试 | Cypress | 最少 | 最慢 | 完整业务流程 |
