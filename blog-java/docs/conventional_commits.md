# Conventional Commits 规范

## 概述

Conventional Commits 是一种轻量级的提交规范约定，通过规范化的提交信息，使人更容易阅读、自动化工具更容易解析。

## 基本格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

- **type**（必填）：提交类型
- **scope**（可选）：影响范围
- **subject**（必填）：简短描述
- **body**（可选）：详细描述
- **footer**（可选）：关联信息

---

## Type 类型

| Type | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | feat: 添加用户登录功能 |
| `fix` | 修复 Bug | fix: 修复登录页面验证错误 |
| `docs` | 文档更新 | docs: 更新 README 安装说明 |
| `style` | 代码格式（不影响功能） | style: 统一缩进为 2 空格 |
| `refactor` | 重构（不是新功能也不是修复） | refactor: 重构 useFetch Hook |
| `perf` | 性能优化 | perf: 优化列表渲染性能 |
| `test` | 测试相关 | test: 添加 useFetch 单元测试 |
| `build` | 构建系统或依赖 | build: 升级 vite 到 7.0 |
| `ci` | CI/CD 配置 | ci: 添加 GitHub Actions 工作流 |
| `chore` | 其他修改 | chore: 更新 .gitignore |
| `revert` | 回滚提交 | revert: 撤销 feat(xxx) |

---

## Scope 影响范围

Scope 表示本次提交影响的模块或组件，需用括号包裹。

常见示例：

```
feat(auth): 添加 OAuth 登录支持
fix(userlist): 修复用户列表分页问题
docs(api): 更新 API 接口文档
style(useFetch): 统一代码格式
```

---

## Subject 简短描述

- 使用中文或英文
- 以动词开头，使用第一人称现在时
- 首字母小写
- 结尾不加句号
- 限制在 50 个字符以内

```
✅ good
feat: 添加用户头像上传功能
fix: 修复登录验证失败的问题

❌ bad
Feat: 添加了用户头像上传功能。
fix: 修复了登录验证失败的问题。
```

---

## Body 详细描述（可选）

补充说明本次提交的动机和实现细节：

- 每行限制在 72 个字符以内
- 解释 **what** 和 **why**，而非 **how**

```
feat(useFetch): 添加请求重试逻辑

当网络请求失败时，自动重试最多 3 次，每次间隔
时间递增（1s, 2s, 3s），避免频繁请求导致
服务器压力过大。

Closes #123
```

---

## Footer 关联信息（可选）

### Breaking Changes

不兼容的修改必须以 `BREAKING CHANGE:` 开头：

```
feat(api): 移除用户列表接口

BREAKING CHANGE: 用户列表接口已废弃，请使用新的
getUserListV2 接口替代。
```

### 关联 Issue

```
fix(auth): 修复 token 过期问题

Closes #234
Refs #123
```

---

## 完整示例

### 简单提交
```
feat: 添加用户注册功能
```

### 带 Scope 的提交
```
fix(useFetch): 修复请求未正确取消的问题
```

### 带 Body 的提交
```
feat(userlist): 添加无限滚动加载

使用 IntersectionObserver 实现虚拟滚动，
当用户滚动到列表底部时自动加载更多数据。

Closes #456
```

### Breaking Change
```
refactor(api): 统一接口返回格式

BREAKING CHANGE: 所有接口返回格式从 `{ data, code }`
改为 `{ data, status, message }`，请同步更新
调用处的类型定义。
```

---

## 工具推荐

| 工具 | 用途 |
|------|------|
| [commitizen](https://github.com/commitizen/cz-cli) | 交互式生成提交信息 |
| [commitlint](https://github.com/conventional-changelog/commitlint) | Lint 提交信息 |
| [husky](https://github.com/typicode/husky) | Git hooks 管理 |
| [standard-version](https://github.com/conventional-changelog/standard-version) | 自动生成 Changelog |

---

## 参考资源

- [Conventional Commits 官方规范](https://www.conventionalcommits.org/)
- [Angular 提交规范](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit)
- [Semantic Versioning](https://semver.org/)
