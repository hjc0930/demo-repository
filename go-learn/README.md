# Go Demo - Go 语言学习项目

一个综合性的 Go 语言学习项目，涵盖核心概念和工程实践。

## 项目结构

```
go-demo/
├── cmd/                    # 应用程序入口
│   ├── api/               # HTTP API 服务
│   │   └── main.go
│   └── cli/               # CLI 命令行工具
│       └── main.go
├── internal/              # 私有代码（不可被外部导入）
│   ├── models/            # 数据模型
│   │   ├── todo.go
│   │   └── todo_test.go
│   ├── storage/           # 存储层
│   │   └── memory.go
│   ├── service/           # 业务逻辑层
│   │   ├── todo.go
│   │   └── todo_test.go
│   └── handler/           # HTTP 处理器
│       └── handler.go
├── pkg/                   # 公共代码（可被外部导入）
│   ├── concurrency/       # 并发示例
│   │   └── concurrency.go
│   └── utils/             # 工具函数
├── docs/                  # Swagger 文档（自动生成）
│   ├── docs.go
│   ├── swagger.json
│   └── swagger.yaml
├── configs/               # 配置文件
├── go.mod                 # 模块定义
└── go.sum                 # 依赖校验
```

## 快速开始

### 1. 初始化项目

```bash
cd go-demo
go mod tidy
```

### 2. 运行 HTTP API 服务

```bash
go run ./cmd/api
```

访问 http://localhost:8080 查看 API 文档

### 3. Swagger API 文档

项目集成了 Swagger 文档，启动服务后访问：

```
http://localhost:8080/swagger/index.html
```

#### Swagger 常用命令

```bash
# 安装 swag 命令行工具（首次使用）
go install github.com/swaggo/swag/cmd/swag@latest

# 生成/更新 Swagger 文档
swag init -g cmd/api/main.go -o ./docs

# 生成的文件：
# - docs/docs.go      # Go 代码
# - docs/swagger.json # JSON 格式规范
# - docs/swagger.yaml # YAML 格式规范
```

#### 添加新的 API 文档注解

在 handler 方法上添加注解：

```go
// @Summary      简短描述
// @Description  详细描述
// @Tags         分组标签
// @Accept       json
// @Produce      json
// @Param        id   path      int  true  "参数说明"
// @Success      200  {object}  Response
// @Failure      400  {object}  Response
// @Router       /todos/{id} [get]
func (h *TodoHandler) Get(c *gin.Context) {
    // ...
}
```

修改注解后记得重新生成文档：

```bash
swag init -g cmd/api/main.go -o ./docs
```

### 4. 运行 CLI 工具

```bash
# 查看帮助
go run ./cmd/cli help

# 添加任务
go run ./cmd/cli add -title "学习 Go" -priority high

# 列出任务
go run ./cmd/cli list

# 交互模式
go run ./cmd/cli interactive
```

### 5. 运行测试

```bash
# 运行所有测试
go test ./...

# 查看覆盖率
go test -cover ./...

# 运行基准测试
go test -bench=. ./...
```

## 核心知识点

### 1. 基础语法 (`internal/models/todo.go`)
- 变量声明与类型
- 结构体与方法
- 指针
- 接口
- 错误处理

### 2. 并发编程 (`pkg/concurrency/concurrency.go`)
- Goroutine
- Channel（有缓冲/无缓冲）
- Select 多路复用
- sync 包（Mutex, WaitGroup）
- Context 上下文
- Worker Pool 模式
- Pipeline 管道模式
- Fan-Out/Fan-In 扇出扇入

### 3. 存储层 (`internal/storage/memory.go`)
- 接口设计
- 读写锁 (sync.RWMutex)
- 文件操作
- JSON 序列化

### 4. HTTP 服务 (`cmd/api/main.go`, `internal/handler/handler.go`)
- 标准库 net/http
- Gin 框架
- 路由
- 中间件
- JSON 处理
- 优雅关闭
- Swagger API 文档

### 5. CLI 工具 (`cmd/cli/main.go`)
- flag 包参数解析
- 交互式命令
- 文件读写

### 6. 测试 (`internal/models/todo_test.go`)
- 单元测试
- 表驱动测试
- 基准测试
- 示例测试

## API 端点

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/todos | 获取任务列表 |
| POST | /api/todos | 创建任务 |
| GET | /api/todos/{id} | 获取单个任务 |
| PUT | /api/todos/{id} | 更新任务 |
| DELETE | /api/todos/{id} | 删除任务 |
| GET | /api/stats | 获取统计信息 |

### 示例请求

```bash
# 创建任务
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"学习 Go","priority":"high"}'

# 获取列表
curl http://localhost:8080/api/todos

# 更新任务
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"completed":true}'

# 删除任务
curl -X DELETE http://localhost:8080/api/todos/1
```

## 学习路径

1. **基础** - 阅读 `internal/models/todo.go`，理解结构体、方法、接口
2. **并发** - 运行 `pkg/concurrency/concurrency.go` 中的示例
3. **存储** - 理解 `internal/storage/` 中的接口设计和实现
4. **服务** - 学习 `internal/service/` 中的业务逻辑组织
5. **HTTP** - 运行 API 服务，理解路由和中间件
6. **CLI** - 使用 CLI 工具，理解命令行参数处理
7. **测试** - 阅读测试代码，理解 Go 的测试模式

## 代码注释

所有代码都包含详细的中文注释，解释 Go 语言的核心概念和最佳实践。

## 扩展练习

1. 添加用户认证中间件
2. 实现 SQLite 数据库存储
3. 添加分页功能
4. 实现 WebSocket 实时通知
5. 添加配置管理（使用 Viper）
6. 添加日志（使用 Zap）
