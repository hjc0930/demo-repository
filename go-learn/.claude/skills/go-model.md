# Go Model Generator

Generate a new Go data model with proper structure, methods, and documentation.

## Usage

```
/go-model <ModelName> [field1:Type field2:Type...]
```

## Examples

```
/go-model User name:string email:string age:int
/go-model Product name:string price:float64 stock:int
/go-model Order userID:uint items:[]string total:float64
```

## Instructions

When this skill is invoked, generate a complete Go model file following these steps:

### 1. Create the model file at `internal/models/{name}.go`

### 2. Include these components:

**Package header with documentation:**
```go
// Package models 定义数据模型
// ============================================================
// {ModelName} 模型说明
// ============================================================
package models

import (
    "encoding/json"
    "time"
)
```

**Struct definition with JSON tags:**
```go
// {ModelName} 表示一个{实体描述}
type {ModelName} struct {
    ID        uint      `json:"id"`
    // ... fields from user input
    CreatedAt time.Time `json:"created_at"`
    UpdatedAt time.Time `json:"updated_at"`
}
```

**Constructor function:**
```go
// New{ModelName} 创建新的{ModelName}实例
func New{ModelName}(...) *{ModelName} {
    now := time.Now()
    return &{ModelName}{
        // ...
        CreatedAt: now,
        UpdatedAt: now,
    }
}
```

**Methods:**
```go
// ToJSON 实现 JSONAble 接口
func (m *{ModelName}) ToJSON() ([]byte, error) {
    return json.Marshal(m)
}

// FromJSON 从 JSON 数据解析
func (m *{ModelName}) FromJSON(data []byte) error {
    return json.Unmarshal(data, m)
}
```

### 3. Create test file at `internal/models/{name}_test.go`

Include:
- Table-driven tests for `New{ModelName}`
- Tests for all methods
- JSON serialization tests
- Benchmark tests

### 4. Follow project conventions:

- Use Chinese comments for documentation
- Use pointer receivers for methods
- Include detailed struct field comments
- Add validation methods if appropriate
