# Todo Backend

A Todo management REST API built with Go + Gin + GORM.

[中文文档](README.zh-CN.md)

## Tech Stack

- Go 1.26 + Gin (Web framework)
- GORM (ORM)
- SQLite (local development) / MySQL 8.0 (production)
- Swagger (API documentation)
- Docker + Docker Compose (containerized deployment)

## Project Structure

```
backend/
├── cmd/server/main.go          # Application entry point
├── internal/
│   ├── config/config.go        # Configuration loader
│   ├── handler/todo.go         # HTTP handlers (controllers)
│   ├── model/
│   │   ├── todo.go             # Database model
│   │   ├── dto.go              # Request/response DTOs
│   │   └── result.go           # Unified response wrapper
│   ├── repository/todo.go      # Data access layer
│   └── service/todo.go         # Business logic layer
├── pkg/apperror/error.go       # Custom business error
├── docs/                       # Auto-generated Swagger docs
├── scripts/schema.sql          # MySQL schema
├── deployments/
│   ├── Dockerfile              # Multi-stage build image
│   └── docker-compose.yml      # MySQL + App orchestration
├── .env.example                # Environment variable template
├── Makefile                    # Build shortcuts
├── go.mod
└── go.sum
```

## Prerequisites

| Tool                    | Purpose                                                  |
| ----------------------- | -------------------------------------------------------- |
| Go 1.21+                | Compile and run                                          |
| make                    | Execute Makefile commands (pre-installed on macOS/Linux) |
| Docker + Docker Compose | Containerized deployment (optional)                      |
| swag                    | Generate Swagger docs (optional)                         |

Install swag (only needed when regenerating API docs):

```bash
go install github.com/swaggo/swag/cmd/swag@latest
```

## Getting Started

### Option 1: Local Development (SQLite, no database installation required)

1. Navigate to the project directory:

```bash
cd backend
```

2. Install dependencies:

```bash
go mod download
```

3. Create the config file:

```bash
cp .env.example .env
```

The default config uses SQLite, no changes needed:

```env
SERVER_PORT=8080
DB_DRIVER=sqlite
```

4. Start the server:

```bash
make run
```

The server will be available at `http://localhost:8080`.

### Option 2: Docker Compose Deployment (MySQL)

1. Navigate to the deployment directory:

```bash
cd backend/deployments
```

2. Start all services:

```bash
docker-compose up -d
```

This starts two containers:

- **todo-mysql**: MySQL 8.0, automatically initializes the database and table schema
- **todo-app**: Go application, waits for MySQL to be healthy before starting

3. View logs:

```bash
docker-compose logs -f app
```

4. Stop services:

```bash
docker-compose down        # Stop and retain data
docker-compose down -v     # Stop and remove data
```

Default MySQL configuration for Docker:

| Setting  | Value     |
| -------- | --------- |
| Host     | localhost |
| Port     | 3306      |
| User     | root      |
| Password | 123456    |
| Database | todo_db   |

## Environment Variables

| Variable      | Default     | Description                          |
| ------------- | ----------- | ------------------------------------ |
| `SERVER_PORT` | `8080`      | Server port                          |
| `DB_DRIVER`   | `sqlite`    | Database driver, `sqlite` or `mysql` |
| `DB_HOST`     | `localhost` | MySQL host                           |
| `DB_PORT`     | `3306`      | MySQL port                           |
| `DB_USER`     | `root`      | MySQL username                       |
| `DB_PASSWORD` | `123456`    | MySQL password                       |
| `DB_NAME`     | `todo_db`   | MySQL database name                  |

To use MySQL, set `DB_DRIVER` to `mysql` and configure the connection details:

```env
SERVER_PORT=8080
DB_DRIVER=mysql
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_PASSWORD=123456
DB_NAME=todo_db
```

## API Reference

All endpoints are prefixed with `/api/v1`.

### Unified Response Format

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### Endpoints

#### 1. List Todos

```
GET /api/v1/todos
```

Query Parameters:

| Parameter  | Type | Required | Description                                        |
| ---------- | ---- | -------- | -------------------------------------------------- |
| `status`   | int  | No       | Filter by status: 0=pending, 1=in-progress, 2=done |
| `page`     | int  | No       | Page number, default 1                             |
| `pageSize` | int  | No       | Items per page, default 10                         |

Response example:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "Learn Go",
        "description": "Complete Gin framework study",
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

#### 2. Get Todo by ID

```
GET /api/v1/todos/:id
```

Response example:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Learn Go",
    "description": "Complete Gin framework study",
    "status": 0,
    "priority": 1,
    "dueDate": null,
    "createdAt": "2024-01-01T00:00:00Z",
    "updatedAt": "2024-01-01T00:00:00Z"
  }
}
```

#### 3. Create Todo

```
POST /api/v1/todos
```

Request Body:

| Field         | Type   | Required | Description                                        |
| ------------- | ------ | -------- | -------------------------------------------------- |
| `title`       | string | Yes      | Title, max 255 characters                          |
| `description` | string | No       | Description, max 2000 characters                   |
| `status`      | int    | No       | Status: 0=pending (default), 1=in-progress, 2=done |
| `priority`    | int    | No       | Priority: 0=low, 1=medium (default), 2=high        |
| `dueDate`     | string | No       | Due date, ISO 8601 format                          |

Request example:

```json
{
  "title": "Learn Go",
  "description": "Complete Gin framework study",
  "priority": 1,
  "dueDate": "2024-12-31T23:59:59Z"
}
```

#### 4. Update Todo

```
PUT /api/v1/todos/:id
```

Request Body (partial update — only include fields you want to change):

| Field         | Type   | Required | Description                      |
| ------------- | ------ | -------- | -------------------------------- |
| `title`       | string | No       | Title, max 255 characters        |
| `description` | string | No       | Description, max 2000 characters |
| `status`      | int    | No       | Status                           |
| `priority`    | int    | No       | Priority                         |
| `dueDate`     | string | No       | Due date                         |

Request example:

```json
{
  "status": 1,
  "priority": 2
}
```

#### 5. Delete Todo

```
DELETE /api/v1/todos/:id
```

Soft delete — data is not physically removed from the database.

Response example:

```json
{
  "code": 200,
  "message": "success"
}
```

## Data Model

### Todo

| Field         | Type        | Description                              |
| ------------- | ----------- | ---------------------------------------- |
| `id`          | uint        | Auto-increment primary key               |
| `title`       | string(255) | Title, required                          |
| `description` | text        | Description                              |
| `status`      | tinyint     | Status: 0=pending, 1=in-progress, 2=done |
| `priority`    | tinyint     | Priority: 0=low, 1=medium, 2=high        |
| `dueDate`     | datetime    | Due date, nullable                       |
| `createdAt`   | datetime    | Creation time                            |
| `updatedAt`   | datetime    | Last update time                         |
| `deletedAt`   | datetime    | Deletion time (soft delete)              |

## Swagger Documentation

After starting the server, visit in your browser:

```
http://localhost:8080/swagger/index.html
```

You can test all API endpoints directly from the Swagger UI.

If you modify Swagger annotations in the handler files, regenerate the docs:

```bash
make swagger
```

## Go Dependency Management

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

## Makefile Commands

| Command        | Description                        |
| -------------- | ---------------------------------- |
| `make run`     | Start development server           |
| `make build`   | Compile binary to `bin/` directory |
| `make swagger` | Regenerate Swagger docs            |
| `make test`    | Run tests                          |
| `make clean`   | Remove build artifacts             |

## Quick Test

```bash
# Create a todo
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Go","description":"Complete Gin framework study","priority":1}'

# List all todos
curl http://localhost:8080/api/v1/todos

# Filter by status
curl "http://localhost:8080/api/v1/todos?status=0&page=1&pageSize=10"

# Get todo by ID
curl http://localhost:8080/api/v1/todos/1

# Update a todo
curl -X PUT http://localhost:8080/api/v1/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"status":1}'

# Delete a todo
curl -X DELETE http://localhost:8080/api/v1/todos/1
```
