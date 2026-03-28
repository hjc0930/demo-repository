# Go Handler Generator

Generate a new HTTP handler following the project's layered architecture.

## Usage

```
/go-handler <ResourceName> [fields...]
```

## Examples

```
/go-handler User name:string email:string
/go-handler Product name:string price:float64
```

## Instructions

When this skill is invoked, generate a complete CRUD handler following these steps:

### 1. Ensure model exists

Check if `internal/models/{resource}.go` exists. If not, suggest running `/go-model` first.

### 2. Create storage implementation at `internal/storage/{resource}.go`

Implement the `Repository` interface methods:
- Create
- GetByID
- GetAll
- Update
- Delete

### 3. Create service at `internal/service/{resource}.go`

Include:
- Request/Response DTOs
- CRUD methods
- Validation logic
- Error types

### 4. Create handler at `internal/handler/{resource}_handler.go`

Include:
- Handler struct with service dependency
- Constructor function
- ServeHTTP method for routing
- CRUD handlers (List, Create, Get, Update, Delete)
- Error handling

### 5. Register routes

Show how to register in `cmd/api/main.go`:
```go
{resource}Handler := handler.New{Resource}Handler({resource}Service)
mux.Handle("/api/{resources}", {resource}Handler)
mux.Handle("/api/{resources}/", {resource}Handler)
```

### 6. Create tests

Generate test files for:
- `internal/service/{resource}_test.go`
- `internal/handler/{resource}_handler_test.go`

## Template

```go
// Package handler - HTTP 处理器
package handler

type {Resource}Handler struct {
    service *service.{Resource}Service
}

func New{Resource}Handler(svc *service.{Resource}Service) *{Resource}Handler {
    return &{Resource}Handler{service: svc}
}

func (h *{Resource}Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
    // Route to appropriate handler method
}

func (h *{Resource}Handler) List(w http.ResponseWriter, r *http.Request) { }
func (h *{Resource}Handler) Create(w http.ResponseWriter, r *http.Request) { }
func (h *{Resource}Handler) Get(w http.ResponseWriter, r *http.Request, id uint) { }
func (h *{Resource}Handler) Update(w http.ResponseWriter, r *http.Request, id uint) { }
func (h *{Resource}Handler) Delete(w http.ResponseWriter, r *http.Request, id uint) { }
```
