# Go Concurrency Pattern Generator

Generate a concurrency pattern example with detailed documentation.

## Usage

```
/go-concurrency <pattern> [options]
```

## Available Patterns

- `worker-pool` - Worker pool pattern
- `pipeline` - Pipeline pattern
- `fan-out` - Fan-out pattern
- `fan-in` - Fan-in pattern
- `pub-sub` - Publish-Subscribe pattern
- `rate-limiter` - Rate limiting
- `circuit-breaker` - Circuit breaker

## Examples

```
/go-concurrency worker-pool workers:5
/go-concurrency pipeline stages:3
/go-concurrency rate-limiter rps:100
```

## Instructions

When this skill is invoked:

### 1. Add the pattern to `pkg/concurrency/concurrency.go`

Include:
- Detailed Chinese comments explaining the pattern
- Complete working implementation
- Demo function with descriptive output

### 2. Pattern structure:

```go
// ============================================================
// {PatternName} 模式
// ------------------------------------------------------------
// 模式说明：
// - 适用场景
// - 优缺点
// - 注意事项
// ============================================================

// {PatternName}Config 配置
type {PatternName}Config struct {
    // ...
}

// {PatternName} 实现
type {PatternName} struct {
    // ...
}

// New{PatternName} 创建实例
func New{PatternName}(config {PatternName}Config) *{PatternName} {
    // ...
}

// Run 执行
func (p *{PatternName}) Run() {
    // ...
}

// {PatternName}Demo 演示函数
func {PatternName}Demo() {
    fmt.Println("\n=== {PatternName} 示例 ===")
    // Demo implementation with output
}
```

### 3. Update `cmd/concurrency/main.go`

Add the new pattern to the menu:
- Add to the help text
- Add a case in the switch statement
- Add to the RunAllDemos function

### 4. Include practical example

Show a real-world use case, such as:
- Processing HTTP requests
- Database operations
- File processing
- API calls

### 5. Add safety considerations

Document:
- How to handle cancellation
- Error handling strategies
- Resource cleanup
- Graceful shutdown
