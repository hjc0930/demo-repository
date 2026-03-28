# Go Learn 项目配置

这是一个 Go 语言学习项目，包含 Web API、CLI 工具和并发示例。

## 项目信息

- **模块名**: `github.com/hjc0930/go-learn`
- **Go 版本**: 1.26+
- **项目类型**: 学习/演示项目

## 目录结构

```
go-demo/
├── cmd/                    # 应用程序入口
│   ├── api/               # HTTP API 服务
│   ├── cli/               # CLI 命令行工具
│   └── concurrency/       # 并发示例
├── internal/              # 私有代码
│   ├── models/            # 数据模型
│   ├── storage/           # 存储层实现
│   ├── service/           # 业务逻辑层
│   └── handler/           # HTTP 处理器
├── pkg/                   # 公共代码
│   ├── concurrency/       # 并发示例
│   └── utils/             # 工具函数
└── configs/               # 配置文件
```

## 常用命令

### 构建

```bash
# 编译所有包
go build ./...

# 编译特定应用
go build -o bin/api ./cmd/api
go build -o bin/cli ./cmd/cli
go build -o bin/concurrency ./cmd/concurrency
```

### 运行

```bash
# 运行 HTTP API 服务
go run ./cmd/api
# 访问 http://localhost:8080

# 运行 CLI 工具
go run ./cmd/cli add -title "任务标题" -priority high
go run ./cmd/cli list
go run ./cmd/cli interactive

# 运行并发示例
go run ./cmd/concurrency
```

### 测试

```bash
# 运行所有测试
go test ./...

# 运行特定包的测试
go test ./internal/models/...

# 查看覆盖率
go test -cover ./...

# 生成覆盖率报告
go test -coverprofile=coverage.out ./...
go tool cover -html=coverage.out

# 运行基准测试
go test -bench=. ./...

# 运行示例测试
go test -v -run Example ./...
```

### 代码质量

```bash
# 格式化代码
go fmt ./...

# 静态检查
go vet ./...

# 整理依赖
go mod tidy
```

## 代码规范

### 命名约定

- **包名**: 小写单词，不使用下划线或驼峰
- **导出标识符**: 首字母大写（PascalCase）
- **私有标识符**: 首字母小写（camelCase）
- **常量**: 使用驼峰命名，不使用全大写
- **接口**: 单方法接口以 "-er" 结尾（如 `Reader`, `Writer`）

### 注释规范

- **包注释**: 在 package 语句前添加，描述包的功能
- **导出函数**: 必须添加注释，以函数名开头
- **代码块**: 使用 `//` 单行注释，复杂逻辑使用块注释说明

```go
// CreateTodo 创建新的待办事项。
// 参数 title 不能为空，priority 必须是有效的优先级值。
func (s *TodoService) CreateTodo(req CreateTodoRequest) (*models.Todo, error) {
    // ...
}
```

### 错误处理

- 不忽略错误，所有错误必须处理
- 使用 `fmt.Errorf` 包装错误，添加上下文
- 自定义错误类型实现 `error` 接口
- 使用 `errors.Is` 和 `errors.As` 判断错误类型

```go
// 正确示例
if err != nil {
    return fmt.Errorf("failed to create todo: %w", err)
}

// 错误示例
if err != nil {
    log.Println(err) // 不要只记录日志
}
```

### 结构体设计

- 使用结构体标签定义 JSON 和验证规则
- 构造函数以 `New` 开头
- 方法接收者统一使用指针类型

```go
type Todo struct {
    ID     uint   `json:"id"`
    Title  string `json:"title"`
    Status Status `json:"status"`
}

func NewTodo(title string) *Todo {
    return &Todo{Title: title}
}
```

## 分层架构

项目采用分层架构，依赖方向为：`handler -> service -> storage -> models`

| 层级 | 职责 | 目录 |
|------|------|------|
| Handler | 处理 HTTP 请求/响应 | `internal/handler/` |
| Service | 业务逻辑 | `internal/service/` |
| Storage | 数据持久化 | `internal/storage/` |
| Models | 数据模型 | `internal/models/` |

## API 设计规范

### RESTful 端点

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/todos | 获取列表 |
| POST | /api/todos | 创建资源 |
| GET | /api/todos/{id} | 获取单个 |
| PUT | /api/todos/{id} | 更新资源 |
| DELETE | /api/todos/{id} | 删除资源 |

### 响应格式

```json
{
  "success": true,
  "data": { ... }
}
```

错误响应：

```json
{
  "success": false,
  "error": {
    "code": 400,
    "message": "错误描述"
  }
}
```

## 并发模式

项目包含以下并发模式的示例（`pkg/concurrency/`）：

1. **Goroutine** - 轻量级线程
2. **Channel** - goroutine 通信
3. **Select** - 多路复用
4. **Context** - 取消和超时控制
5. **Worker Pool** - 工作池模式
6. **Pipeline** - 管道模式
7. **Fan-Out/Fan-In** - 扇出扇入

## 添加新功能

### 添加新的数据模型

1. 在 `internal/models/` 创建模型文件
2. 定义结构体和方法
3. 创建对应的测试文件

### 添加新的 API 端点

1. 在 `internal/models/` 定义数据模型
2. 在 `internal/storage/` 实现存储接口
3. 在 `internal/service/` 实现业务逻辑
4. 在 `internal/handler/` 实现 HTTP 处理器
5. 在 `cmd/api/main.go` 注册路由

### 添加新的并发示例

1. 在 `pkg/concurrency/concurrency.go` 添加示例函数
2. 在 `cmd/concurrency/main.go` 添加菜单选项

## 依赖管理

```bash
# 添加依赖
go get github.com/some/package

# 添加特定版本
go get github.com/some/package@v1.2.3

# 更新依赖
go get -u ./...

# 清理未使用的依赖
go mod tidy
```

## 注意事项

- 所有代码必须有中文注释，便于学习理解
- 新增功能需要添加对应的单元测试
- 遵循 Go 的惯用写法（idiomatic Go）
- 使用 `defer` 确保资源释放
