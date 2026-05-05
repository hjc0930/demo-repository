# Todo List - 前端

React + TypeScript + Vite + MUI

## 技术栈

- **框架**: React 19 + TypeScript 6
- **构建工具**: Vite 8
- **UI 组件库**: MUI (Material UI) 9
- **样式方案**: Emotion
- **HTTP 客户端**: Axios
- **路由**: React Router 7
- **日期处理**: Day.js
- **代码规范**: ESLint

## 开发环境快速开始

```bash
pnpm install          # 安装依赖
pnpm dev              # 启动开发服务器
```

## 可用脚本

| 命令 | 说明 |
|------|------|
| `pnpm dev` | 启动开发服务器（热更新） |
| `pnpm build` | TypeScript 类型检查 + 生产构建 |
| `pnpm preview` | 预览生产构建产物 |
| `pnpm lint` | 运行 ESLint 检查 |

## pnpm 常用命令

### 依赖管理

```bash
pnpm add <pkg>               # 安装生产依赖
pnpm add -D <pkg>            # 安装开发依赖
pnpm remove <pkg>            # 卸载依赖
```

### 依赖更新

```bash
pnpm outdated                 # 查看过时依赖
pnpm update                   # semver 范围内更新
pnpm update -i                # 交互式选择更新
pnpm update -i --latest       # 交互式更新（含大版本）
```

### 查看依赖

```bash
pnpm list                     # 查看依赖列表
pnpm why <pkg>                # 查看包为什么被安装
pnpm info <pkg>               # 查看远程包信息
```

### 清理与重装

```bash
pnpm prune                    # 清理未使用的依赖
pnpm store prune              # 清理全局缓存
rm -rf node_modules && pnpm install  # 彻底重装
```

### 生产 / CI 环境

```bash
pnpm install --frozen-lockfile   # 严格按 lockfile 安装
pnpm install --prod              # 只装生产依赖
pnpm prune --prod                # 清理掉 devDependencies
```

## ESLint 配置扩展

如果你正在开发生产级应用，建议更新配置以启用类型感知的 lint 规则：

```js
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // 其他配置...

      // 移除 tseslint.configs.recommended，替换为以下配置
      tseslint.configs.recommendedTypeChecked,
      // 或使用更严格的规则
      tseslint.configs.strictTypeChecked,
      // 可选：风格相关规则
      tseslint.configs.stylisticTypeChecked,

      // 其他配置...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
    },
  },
])
```

你也可以安装 [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) 和 [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) 来获得 React 专属的 lint 规则：

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // 其他配置...
      // 启用 React lint 规则
      reactX.configs['recommended-typescript'],
      // 启用 React DOM lint 规则
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
    },
  },
])
```
