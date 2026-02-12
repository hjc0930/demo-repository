# 测试演示项目 (test-demo)

这是一个用于演示各种测试场景的 React + TypeScript 项目，使用 Vite 构建，Ant Design 作为 UI 组件库。

## 技术栈

- **React 19** - UI 框架
- **TypeScript** - 类型安全
- **Vite** - 构建工具
- **Ant Design 6** - UI 组件库
- **Axios** - HTTP 请求库
- **Vitest** - 单元/集成测试框架
- **React Testing Library** - React 组件测试工具
- **MSW (Mock Service Worker)** - API 模拟
- **Cypress** - E2E 测试框架（专注端到端测试）

## 项目结构

```
src/
├── components/          # React 组件
│   ├── Counter.tsx     # 计数器组件
│   ├── UserForm.tsx    # 用户表单组件
│   ├── UserList.tsx    # 用户列表组件
│   ├── TodoList.tsx    # 待办事项列表组件
│   └── __tests__/      # 组件测试
├── hooks/              # 自定义 Hooks
│   ├── useCounter.ts    # 计数器 Hook
│   ├── useFetch.ts      # 数据获取 Hook
│   └── __tests__/      # Hook 测试
├── services/           # API 服务层
│   ├── api.ts          # Axios 配置
│   ├── userService.ts   # 用户相关 API
│   ├── todoService.ts   # 待办事项 API
│   └── __tests__/      # 服务层测试
├── utils/              # 工具函数
│   ├── format.ts       # 格式化函数
│   ├── validation.ts   # 验证函数
│   └── __tests__/      # 工具函数测试
├── mocks/              # MSW Mock 配置
│   ├── handlers.ts     # Mock 处理器
│   ├── browser.ts      # 浏览器环境 Mock
│   └── server.ts      # Node 环境 Mock
├── cypress/            # Cypress E2E 测试配置
│   ├── e2e/           # E2E 测试用例
│   └── support/        # Cypress 配置和命令
└── types/              # TypeScript 类型定义
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 运行开发服务器

```bash
npm run dev
```

### 运行测试

```bash
# 运行单元/集成测试
npm run test:unit

# 运行测试并监听变化
npm test -- --watch

# 运行测试并生成覆盖率报告
npm run test:coverage

# 运行 E2E 测试（交互模式）
npm run test:e2e

# 运行 E2E 测试（无头模式）
npm run test:e2e:headless
```

### 构建生产版本

```bash
npm run build
```

## 测试覆盖场景

### 1. 组件测试 (src/components/__tests__/)

- **Counter.test.tsx** - 计数器组件测试
  - 初始值渲染
  - 增/减操作
  - 重置功能
  - 最小/最大值限制
  - 回调函数触发

- **UserForm.test.tsx** - 用户表单测试
  - 表单字段渲染
  - 表单验证（必填、格式、长度）
  - 表单提交
  - 初始值支持

- **UserList.test.tsx** - 用户列表测试
  - 列表渲染
  - 数据获取
  - 编辑/删除操作

- **TodoList.test.tsx** - 待办事项测试
  - 列表渲染
  - 添加待办
  - 状态切换
  - 删除操作

### 2. Hook 测试 (src/hooks/__tests__/)

- **useCounter.test.ts** - useCounter Hook 测试
  - 计数功能
  - 边界限制
  - 自定义步长

- **useFetch.test.ts** - useFetch Hook 测试
  - 数据获取
  - 错误处理
  - 回调触发
  - 重新获取功能

### 3. 服务层测试 (src/services/__tests__/)

- **userService.test.ts** - 用户服务测试
  - 登录/注册
  - 获取用户信息
  - 拦截器测试

- **todoService.test.ts** - 待办事项服务测试
  - CRUD 操作
  - 分页支持

### 4. 工具函数测试 (src/utils/__tests__/)

- **format.test.ts** - 格式化函数测试
  - 日期格式化
  - 文件大小格式化
  - 货币格式化
  - 千分位格式化
  - 文本截断

- **validation.test.ts** - 验证函数测试
  - 邮箱验证
  - 手机号验证
  - 身份证验证
  - URL 验证

### 5. E2E 测试 (cypress/e2e/)

- **counter.cy.ts** - 计数器页面 E2E 测试（页面导航、计数操作、边界限制）
- **tabs.cy.ts** - 标签页导航测试（标签切换、状态保持）
- **user-form.cy.ts** - 用户表单 E2E 测试（表单填写、验证、提交流程）
- **user-list.cy.ts** - 用户列表 E2E 测试（列表渲染、编辑/删除操作）
- **todo-list.cy.ts** - 待办事项 E2E 测试（添加待办、状态切换、删除操作）
- **layout.cy.ts** - 页面布局测试（头部/侧边栏、响应式布局）
- **workflow.cy.ts** - 完整用户流程测试（多页面流程、键盘导航）

### 6. E2E 测试 (cypress/e2e/)

## API Mock

项目使用 MSW (Mock Service Worker) 来模拟 API 请求：

- **登录**: `POST /api/auth/login`
  - 用户名: `admin`, 密码: `123456`

- **注册**: `POST /api/auth/register`

- **获取用户**: `GET /api/user/info` / `GET /api/user/list`

- **待办事项**: `GET/POST/PUT/DELETE /api/todos`

## 学习要点

### 组件测试
- 使用 React Testing Library 的 `render` 渲染组件
- 使用 `screen` 查询元素
- 使用 `fireEvent` 或 `userEvent` 模拟用户交互
- 使用 `waitFor` 处理异步状态

### Hook 测试
- 使用 `renderHook` 测试自定义 Hooks
- 测试状态变化和副作用
- 测试错误边界情况

### API 测试
- 使用 MSW 模拟 HTTP 请求
- 测试成功和失败场景
- 测试请求/响应拦截器

### 工具函数测试
- 纯函数测试，无需渲染
- 测试边界情况
- 使用 `describe` 分组相关测试

### E2E 测试 (Cypress)
- 使用 `cy.visit()` 访问页面
- 使用 `cy.contains()` 查找元素
- 使用 `cy.get()` 选择元素
- 使用 `.click()`, `.type()` 模拟用户操作
- 使用 `.should()` 断言验证
- 测试完整用户流程和页面交互
- **注意**：Cypress 只做 E2E 测试，组件测试由 Vitest 负责

## 注意事项

1. **Ant Design 6.x 变化**: 部分测试由于 Ant Design 6.x API 变化可能需要调整
2. **Vitest 测试环境**: 使用 jsdom 模拟浏览器环境
3. **异步操作**: 使用 `waitFor` 或 `findBy` 查询处理异步更新
4. **E2E 测试**: 运行 E2E 测试前需先启动开发服务器 `npm run dev`
5. **端口配置**: Cypress 默认使用 `http://localhost:5173`，Vite 默认端口为 5173
6. **测试职责划分**:
   - **Vitest** 负责单元/集成测试（组件、Hook、服务、工具函数）
   - **Cypress** 专注于 E2E 测试（完整用户流程）
   - **不要**用 Cypress 做组件测试，这会导致职责重叠和维护困难

## License

MIT
