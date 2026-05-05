# Todo Backend

基于 Go + Gin + GORM 的 Todo 管理 REST API 服务。

## 技术栈

- Go 1.26 + Gin (Web 框架)
- GORM (ORM)
- SQLite (本地开发) / MySQL 8.0 (生产环境)
- Swagger (API 文档)
- Docker + Docker Compose (容器化部署)

## 项目结构

```
backend/
├── cmd/server/main.go          # 入口文件
├── internal/
│   ├── config/config.go        # 配置加载
│   ├── handler/todo.go         # HTTP 处理器（控制器）
│   ├── model/
│   │   ├── todo.go             # 数据库模型
│   │   ├── dto.go              # 请求/响应数据结构
│   │   └── result.go           # 统一响应格式
│   ├── repository/todo.go      # 数据访问层
│   └── service/todo.go         # 业务逻辑层
├── pkg/apperror/error.go       # 自定义业务错误
├── docs/                       # Swagger 自动生成的文档
├── scripts/schema.sql          # MySQL 建表脚本
├── deployments/
│   ├── Dockerfile              # 多阶段构建镜像
│   └── docker-compose.yml      # 编排 MySQL + App
├── .env.example                # 环境变量模板
├── Makefile                    # 常用命令快捷方式
├── go.mod
└── go.sum
```

## 环境要求

| 工具 | 用途 |
|------|------|
| Go 1.21+ | 编译运行 |
| make | 执行 Makefile 命令（macOS/Linux 自带） |
| Docker + Docker Compose | 容器化部署（可选） |
| swag | 生成 Swagger 文档（可选） |

安装 swag（仅在需要重新生成 API 文档时）：

```bash
go install github.com/swaggo/swag/cmd/swag@latest
```

## 快速开始

### 方式一：本地开发（SQLite，无需安装数据库）

1. 进入项目目录：

```bash
cd backend
```

2. 安装依赖：

```bash
go mod download
```

3. 创建配置文件：

```bash
cp .env.example .env
```

默认配置使用 SQLite，无需修改：

```env
SERVER_PORT=8080
DB_DRIVER=sqlite
```

4. 启动服务：

```bash
make run
```

服务启动后访问 `http://localhost:8080`。

### 方式二：Docker Compose 部署（MySQL）

1. 进入部署目录：

```bash
cd backend/deployments
```

2. 启动所有服务：

```bash
docker-compose up -d
```

这会启动两个容器：
- **todo-mysql**：MySQL 8.0，自动初始化数据库和表结构
- **todo-app**：Go 应用，等待 MySQL 就绪后自动启动

3. 查看日志：

```bash
docker-compose logs -f app
```

4. 停止服务：

```bash
docker-compose down        # 停止但保留数据
docker-compose down -v     # 停止并清除数据
```

Docker 方式下的默认 MySQL 配置：

| 配置项 | 值 |
|--------|-----|
| Host | localhost |
| Port | 3306 |
| User | root |
| Password | 123456 |
| Database | todo_db |

## 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `SERVER_PORT` | `8080` | 服务端口 |
| `DB_DRIVER` | `sqlite` | 数据库驱动，可选 `sqlite` 或 `mysql` |
| `DB_HOST` | `localhost` | MySQL 主机地址 |
| `DB_PORT` | `3306` | MySQL 端口 |
| `DB_USER` | `root` | MySQL 用户名 |
| `DB_PASSWORD` | `123456` | MySQL 密码 |
| `DB_NAME` | `todo_db` | MySQL 数据库名 |

使用 MySQL 时，将 `DB_DRIVER` 改为 `mysql` 并配置对应的连接信息：

```env
SERVER_PORT=8080
DB_DRIVER=mysql
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_PASSWORD=123456
DB_NAME=todo_db
```

## API 接口

所有接口前缀为 `/api/v1`。

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 接口列表

#### 1. 获取 Todo 列表

```
GET /api/v1/todos
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `status` | int | 否 | 按状态筛选：0=待办，1=进行中，2=已完成 |
| `page` | int | 否 | 页码，默认 1 |
| `pageSize` | int | 否 | 每页条数，默认 10 |

响应示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "学习 Go",
        "description": "完成 Gin 框架学习",
        "status": 0,
        "priority": 1,
        "dueDate": null,
        "createdAt": "2024-01-01T00:00:00Z",
        "updatedAt": "2024-01-01T00:00:00Z"
      }
    ],
    "total": 1,
    "page": 1,
    "pageSize": 10
  }
}
```

#### 2. 获取 Todo 详情

```
GET /api/v1/todos/:id
```

响应示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "学习 Go",
    "description": "完成 Gin 框架学习",
    "status": 0,
    "priority": 1,
    "dueDate": null,
    "createdAt": "2024-01-01T00:00:00Z",
    "updatedAt": "2024-01-01T00:00:00Z"
  }
}
```

#### 3. 创建 Todo

```
POST /api/v1/todos
```

请求体：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `title` | string | 是 | 标题，最长 255 字符 |
| `description` | string | 否 | 描述，最长 2000 字符 |
| `status` | int | 否 | 状态：0=待办（默认），1=进行中，2=已完成 |
| `priority` | int | 否 | 优先级：0=低，1=中（默认），2=高 |
| `dueDate` | string | 否 | 截止日期，ISO 8601 格式 |

请求示例：

```json
{
  "title": "学习 Go",
  "description": "完成 Gin 框架学习",
  "priority": 1,
  "dueDate": "2024-12-31T23:59:59Z"
}
```

#### 4. 更新 Todo

```
PUT /api/v1/todos/:id
```

请求体（支持部分更新，只传需要修改的字段）：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `title` | string | 否 | 标题，最长 255 字符 |
| `description` | string | 否 | 描述，最长 2000 字符 |
| `status` | int | 否 | 状态 |
| `priority` | int | 否 | 优先级 |
| `dueDate` | string | 否 | 截止日期 |

请求示例：

```json
{
  "status": 1,
  "priority": 2
}
```

#### 5. 删除 Todo

```
DELETE /api/v1/todos/:id
```

软删除，数据不会真正从数据库中移除。

响应示例：

```json
{
  "code": 200,
  "message": "success"
}
```

## 数据模型

### Todo

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | uint | 自增主键 |
| `title` | string(255) | 标题，必填 |
| `description` | text | 描述 |
| `status` | tinyint | 状态：0=待办，1=进行中，2=已完成 |
| `priority` | tinyint | 优先级：0=低，1=中，2=高 |
| `dueDate` | datetime | 截止日期，可为空 |
| `createdAt` | datetime | 创建时间 |
| `updatedAt` | datetime | 更新时间 |
| `deletedAt` | datetime | 删除时间（软删除） |

## Swagger 文档

服务启动后，浏览器访问：

```
http://localhost:8080/swagger/index.html
```

可以直接在页面上调试所有 API 接口。

如果修改了 handler 中的 Swagger 注释，需要重新生成文档：

```bash
make swagger
```

## Go 依赖管理

### go mod 常用命令

```bash
go mod init <module>          # 初始化新模块，生成 go.mod
go mod tidy                   # 整理依赖：添加缺失的、移除未使用的
go mod download               # 下载所有依赖到本地缓存
go mod verify                 # 验证依赖的完整性（校验哈希）
go mod graph                  # 查看依赖图（直接和间接依赖）
go mod why <pkg>              # 查看为什么需要某个依赖
go mod vendor                 # 将依赖复制到 vendor/ 目录
```

### 依赖管理

```bash
go get <pkg>@latest           # 添加或更新依赖到最新版本
go get <pkg>@v1.2.3           # 安装指定版本
go get <pkg>@<commit>         # 安装指定 commit

go get -u <pkg>               # 更新依赖到最新的 minor/patch 版本
go get -u=patch <pkg>         # 只更新 patch 版本

go get -u ./...               # 更新所有直接和间接依赖（谨慎使用）
```

> **注意**: Go 1.21+ 中 `go get` 仅用于修改 `go.mod`，构建和运行应使用 `go build` / `go run`。

### 清理与维护

```bash
go clean -modcache            # 清理模块缓存（~3GB+）
go mod tidy                   # 推荐：定期执行以保持依赖整洁
```

### 开发 vs 生产环境

| 场景 | 命令 | 说明 |
|------|------|------|
| 本地开发 | `go mod tidy` | 自动补全和清理依赖 |
| 本地开发 | `go get <pkg>` | 添加新依赖 |
| CI/CD | `go mod download` | 只下载依赖，不修改 go.mod |
| CI/CD | `go mod verify` | 验证依赖未被篡改 |
| 离线部署 | `go mod vendor` | 将依赖打入 vendor/ 用于离线构建 |

## Makefile 命令

| 命令 | 说明 |
|------|------|
| `make run` | 启动开发服务器 |
| `make build` | 编译二进制文件到 `bin/` 目录 |
| `make swagger` | 重新生成 Swagger 文档 |
| `make test` | 运行测试 |
| `make clean` | 清理构建产物 |

## 快速测试

```bash
# 创建 Todo
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"学习 Go","description":"完成 Gin 框架学习","priority":1}'

# 查询列表
curl http://localhost:8080/api/v1/todos

# 按状态筛选
curl "http://localhost:8080/api/v1/todos?status=0&page=1&pageSize=10"

# 获取详情
curl http://localhost:8080/api/v1/todos/1

# 更新 Todo
curl -X PUT http://localhost:8080/api/v1/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"status":1}'

# 删除 Todo
curl -X DELETE http://localhost:8080/api/v1/todos/1
```
