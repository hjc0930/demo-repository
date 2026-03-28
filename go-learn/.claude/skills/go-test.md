# Go Test Generator

Generate comprehensive tests for a Go file.

## Usage

```
/go-test <path-to-file>
```

## Examples

```
/go-test internal/models/user.go
/go-test internal/service/product.go
```

## Instructions

When this skill is invoked, generate a test file for the specified Go file:

### 1. Read the source file

Analyze:
- Exported functions and methods
- Struct types
- Interfaces
- Business logic

### 2. Create test file at `{source}_test.go`

### 3. Include these test types:

**Unit tests (table-driven):**
```go
func Test{FunctionName}(t *testing.T) {
    tests := []struct {
        name    string
        input   InputType
        want    OutputType
        wantErr bool
    }{
        // Test cases
    }

    for _, tt := range tests {
        t.Run(tt.name, func(t *testing.T) {
            got, err := {FunctionName}(tt.input)
            if (err != nil) != tt.wantErr {
                t.Errorf("{FunctionName}() error = %v, wantErr %v", err, tt.wantErr)
                return
            }
            if !reflect.DeepEqual(got, tt.want) {
                t.Errorf("{FunctionName}() = %v, want %v", got, tt.want)
            }
        })
    }
}
```

**Method tests:**
```go
func Test{Type}_{Method}(t *testing.T) {
    // Test each method
}
```

**Benchmark tests:**
```go
func Benchmark{FunctionName}(b *testing.B) {
    b.ResetTimer()
    for i := 0; i < b.N; i++ {
        {FunctionName}()
    }
}
```

**Example tests (for documentation):**
```go
func Example{FunctionName}() {
    result := {FunctionName}()
    fmt.Println(result)
    // Output: expected_output
}
```

### 4. Test coverage requirements:

- Test success cases
- Test error cases
- Test edge cases (empty input, nil values, boundary conditions)
- Test concurrent safety if applicable

### 5. Use test helpers:

```go
// Setup helper
func setupTest{Type}() *{Type} {
    return New{Type}()
}

// Assertion helpers (optional)
func assertEqual(t *testing.T, got, want interface{}) {
    t.Helper()
    if !reflect.DeepEqual(got, want) {
        t.Errorf("got %v, want %v", got, want)
    }
}
```

### 6. Add comments explaining test purpose

Use Chinese comments to explain what each test validates.
