# Go Static Server

一个简单的 Go 语言静态文件服务器，支持 SPA（单页应用）路由。

## 功能特性

- 静态文件服务
- SPA 路由支持（History 模式）
- 完善的日志记录
- 跨平台支持

## 目录结构

```
go-statis-server/
├── main.go          # 主程序入口
├── statis/          # 静态资源目录
│   ├── index.html
│   ├── vite.svg
│   └── assets/
│       ├── index-*.css
│       └── index-*.js
└── README.md
```

## 快速开始

### 前置要求

- Go 1.16+

### 运行

```bash
# 进入项目目录
cd go-statis-server

# 运行服务器
go run main.go
```

服务器启动后访问：http://localhost:8080

### 日志示例

```
[启动] 静态资源目录: /path/to/go-statis-server/statis
[启动] 服务器启动中，监听端口: :8080
[启动] 访问地址: http://localhost:8080
[请求] GET /
[成功] 返回静态文件: /index.html
[请求] GET /about
[回退] 文件不存在: /about, 返回 index.html (SPA路由)
```

## 配置

| 配置项       | 默认值   | 说明                                 |
| ------------ | -------- | ------------------------------------ |
| 端口         | 8080     | 在 `main.go` 中修改 `port` 变量      |
| 静态资源目录 | ./statis | 在 `main.go` 中修改 `statisDir` 变量 |

## SPA 路由说明

当请求的文件不存在时，服务器会自动返回 `index.html`，以支持前端框架（如 React、Vue）的 History 路由模式。

## VS Code 调试

### 方法一：直接调试

1. 打开 `main.go`
2. 按 `F5` 启动调试

### 方法二：使用 launch.json

创建 `.vscode/launch.json`：\*\*\*\*

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "name": "Launch Package",
      "type": "go",
      "request": "launch",
      "mode": "auto",
      "program": "${workspaceFolder}",
      "cwd": "${workspaceFolder}"
    }
  ]
}
```

## License

MIT

---
