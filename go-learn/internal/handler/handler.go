// Package handler 提供 HTTP 处理器
// ============================================================
// HTTP 处理器（Handler）
// ------------------------------------------------------------
// Go 标准库 net/http 提供了强大的 HTTP 服务功能
// 处理器需要实现 http.Handler 接口:
//
//	type Handler interface {
//	    ServeHTTP(ResponseWriter, *Request)
//	}
//
// 通常使用 http.HandlerFunc 类型简化处理器的定义
// ============================================================
package handler

import (
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"
	"strings"

	"github.com/hjc0930/go-learn/internal/models"
	"github.com/hjc0930/go-learn/internal/service"
	"github.com/hjc0930/go-learn/internal/storage"
)

// ============================================================
// 响应结构体
// ------------------------------------------------------------
// 统一的 API 响应格式
// ============================================================

// Response 统一响应格式
type Response struct {
	Success bool        `json:"success"`
	Data    interface{} `json:"data,omitempty"`
	Error   *ErrorInfo  `json:"error,omitempty"`
}

// ErrorInfo 错误信息
type ErrorInfo struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

// ============================================================
// TodoHandler 处理任务相关的 HTTP 请求
// ============================================================

// TodoHandler 任务处理器
type TodoHandler struct {
	service *service.TodoService
}

// NewTodoHandler 创建处理器实例
func NewTodoHandler(svc *service.TodoService) *TodoHandler {
	return &TodoHandler{
		service: svc,
	}
}

// ============================================================
// 辅助方法
// ============================================================

// writeJSON 写入 JSON 响应
// 这是一个辅助方法，用于统一处理 JSON 响应
func writeJSON(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(data)
}

// writeSuccess 写入成功响应
func writeSuccess(w http.ResponseWriter, statusCode int, data interface{}) {
	writeJSON(w, statusCode, Response{
		Success: true,
		Data:    data,
	})
}

// writeError 写入错误响应
func writeError(w http.ResponseWriter, statusCode int, message string) {
	writeJSON(w, statusCode, Response{
		Success: false,
		Error: &ErrorInfo{
			Code:    statusCode,
			Message: message,
		},
	})
}

// handleError 统一错误处理
// 根据错误类型返回适当的 HTTP 状态码
func handleError(w http.ResponseWriter, err error) {
	// 使用 errors.As 检查特定错误类型
	var validationErr *service.ValidationError
	if errors.As(err, &validationErr) {
		writeError(w, http.StatusBadRequest, validationErr.Error())
		return
	}

	var businessErr *service.BusinessError
	if errors.As(err, &businessErr) {
		writeError(w, businessErr.Code, businessErr.Message)
		return
	}

	// 使用 errors.Is 检查特定错误值
	if errors.Is(err, storage.ErrNotFound) {
		writeError(w, http.StatusNotFound, "资源未找到")
		return
	}

	// 默认服务器错误
	writeError(w, http.StatusInternalServerError, "服务器内部错误")
}

// ============================================================
// HTTP 处理方法
// ============================================================

// ServeHTTP 实现 http.Handler 接口
// 这是一个路由分发器，根据请求方法和路径分发到具体处理方法
func (h *TodoHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	// 设置 CORS 头（跨域资源共享）
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")

	// 处理预检请求
	if r.Method == http.MethodOptions {
		w.WriteHeader(http.StatusOK)
		return
	}

	// 解析路径
	path := strings.TrimPrefix(r.URL.Path, "/api/todos")
	path = strings.TrimSuffix(path, "/")

	// 路由分发
	if path == "" || path == "/" {
		switch r.Method {
		case http.MethodGet:
			h.List(w, r)
		case http.MethodPost:
			h.Create(w, r)
		default:
			writeError(w, http.StatusMethodNotAllowed, "方法不允许")
		}
		return
	}

	// 解析 ID
	idStr := strings.TrimPrefix(path, "/")
	id, err := strconv.ParseUint(idStr, 10, 64)
	if err != nil {
		writeError(w, http.StatusBadRequest, "无效的 ID")
		return
	}

	// 根据 ID 路由
	switch r.Method {
	case http.MethodGet:
		h.Get(w, r, uint(id))
	case http.MethodPut, http.MethodPatch:
		h.Update(w, r, uint(id))
	case http.MethodDelete:
		h.Delete(w, r, uint(id))
	default:
		writeError(w, http.StatusMethodNotAllowed, "方法不允许")
	}
}

// List 获取任务列表
// GET /api/todos
func (h *TodoHandler) List(w http.ResponseWriter, r *http.Request) {
	// 解析查询参数
	var priority *models.Priority
	if p := r.URL.Query().Get("priority"); p != "" {
		pr := models.Priority(p)
		priority = &pr
	}

	var completed *bool
	if c := r.URL.Query().Get("completed"); c != "" {
		val := strings.ToLower(c) == "true"
		completed = &val
	}

	// 调用服务层
	todos, err := h.service.ListTodos(priority, completed)
	if err != nil {
		handleError(w, err)
		return
	}

	writeSuccess(w, http.StatusOK, todos)
}

// Create 创建任务
// POST /api/todos
func (h *TodoHandler) Create(w http.ResponseWriter, r *http.Request) {
	// 解析请求体
	var req service.CreateTodoRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeError(w, http.StatusBadRequest, "无效的请求体")
		return
	}
	defer r.Body.Close() // 确保关闭请求体

	// 调用服务层
	todo, err := h.service.CreateTodo(req)
	if err != nil {
		handleError(w, err)
		return
	}

	writeSuccess(w, http.StatusCreated, todo)
}

// Get 获取单个任务
// GET /api/todos/{id}
func (h *TodoHandler) Get(w http.ResponseWriter, r *http.Request, id uint) {
	todo, err := h.service.GetTodo(id)
	if err != nil {
		handleError(w, err)
		return
	}

	writeSuccess(w, http.StatusOK, todo)
}

// Update 更新任务
// PUT /api/todos/{id}
func (h *TodoHandler) Update(w http.ResponseWriter, r *http.Request, id uint) {
	var req service.UpdateTodoRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeError(w, http.StatusBadRequest, "无效的请求体")
		return
	}
	defer r.Body.Close()

	todo, err := h.service.UpdateTodo(id, req)
	if err != nil {
		handleError(w, err)
		return
	}

	writeSuccess(w, http.StatusOK, todo)
}

// Delete 删除任务
// DELETE /api/todos/{id}
func (h *TodoHandler) Delete(w http.ResponseWriter, r *http.Request, id uint) {
	if err := h.service.DeleteTodo(id); err != nil {
		handleError(w, err)
		return
	}

	writeSuccess(w, http.StatusOK, map[string]string{
		"message": "删除成功",
	})
}

// ============================================================
// 中间件（Middleware）
// ------------------------------------------------------------
// 中间件是包装 http.Handler 的函数
// 可以在请求到达处理器之前或之后执行代码
// 常用于: 日志、认证、压缩、错误恢复等
// ============================================================

// Middleware 中间件类型
type Middleware func(http.Handler) http.Handler

// LoggingMiddleware 日志中间件
func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// 请求前
		fmt.Printf("[%s] %s %s\n", r.Method, r.URL.Path, r.RemoteAddr)

		// 调用下一个处理器
		next.ServeHTTP(w, r)

		// 请求后（可以在这里记录响应信息）
	})
}

// RecoveryMiddleware 恢复中间件
// 捕获 panic，防止服务器崩溃
func RecoveryMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// defer + recover 捕获 panic
		defer func() {
			if err := recover(); err != nil {
				fmt.Printf("Panic recovered: %v\n", err)
				writeError(w, http.StatusInternalServerError, "服务器内部错误")
			}
		}()

		next.ServeHTTP(w, r)
	})
}

// Chain 中间件链
// 将多个中间件串联起来
func Chain(h http.Handler, middlewares ...Middleware) http.Handler {
	for i := len(middlewares) - 1; i >= 0; i-- {
		h = middlewares[i](h)
	}
	return h
}

// ============================================================
// 统计处理器
// ============================================================

// StatsHandler 统计处理器
type StatsHandler struct {
	service *service.TodoService
}

// NewStatsHandler 创建统计处理器
func NewStatsHandler(svc *service.TodoService) *StatsHandler {
	return &StatsHandler{service: svc}
}

// ServeHTTP 处理统计请求
// GET /api/stats
func (h *StatsHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		writeError(w, http.StatusMethodNotAllowed, "方法不允许")
		return
	}

	stats, err := h.service.GetStats()
	if err != nil {
		handleError(w, err)
		return
	}

	writeSuccess(w, http.StatusOK, stats)
}
