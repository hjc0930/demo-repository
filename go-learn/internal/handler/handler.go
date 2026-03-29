// Package handler 提供 HTTP 处理器
// ============================================================
// HTTP 处理器（Handler）
// ------------------------------------------------------------
// 使用 Gin 框架构建 REST API
// Gin 提供了更简洁的路由、中间件和 JSON 处理
// ============================================================
package handler

import (
	"errors"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
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

// RegisterRoutes 注册路由到 Gin 引擎
func (h *TodoHandler) RegisterRoutes(r *gin.RouterGroup) {
	todos := r.Group("/todos")
	{
		todos.GET("", h.List)
		todos.POST("", h.Create)
		todos.GET("/:id", h.Get)
		todos.PUT("/:id", h.Update)
		todos.DELETE("/:id", h.Delete)
	}
}

// ============================================================
// 辅助方法
// ============================================================

// handleError 统一错误处理
// 根据错误类型返回适当的 HTTP 状态码
func handleError(c *gin.Context, err error) {
	// 使用 errors.As 检查特定错误类型
	var validationErr *service.ValidationError
	if errors.As(err, &validationErr) {
		c.JSON(http.StatusBadRequest, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusBadRequest,
				Message: validationErr.Error(),
			},
		})
		return
	}

	var businessErr *service.BusinessError
	if errors.As(err, &businessErr) {
		c.JSON(businessErr.Code, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    businessErr.Code,
				Message: businessErr.Message,
			},
		})
		return
	}

	// 使用 errors.Is 检查特定错误值
	if errors.Is(err, storage.ErrNotFound) {
		c.JSON(http.StatusNotFound, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusNotFound,
				Message: "资源未找到",
			},
		})
		return
	}

	// 默认服务器错误
	c.JSON(http.StatusInternalServerError, Response{
		Success: false,
		Error: &ErrorInfo{
			Code:    http.StatusInternalServerError,
			Message: "服务器内部错误",
		},
	})
}

// ============================================================
// HTTP 处理方法
// ============================================================

// List 获取任务列表
// GET /api/todos
// @Summary      获取任务列表
// @Description  获取所有待办任务列表，支持按优先级和完成状态筛选
// @Tags         todos
// @Accept       json
// @Produce      json
// @Param        priority   query     string  false  "按优先级筛选 (low/medium/high)"
// @Param        completed query     bool    false  "按完成状态筛选"
// @Success      200       {object}  Response{data=[]models.Todo}
// @Failure      500       {object}  Response
// @Router       /todos [get]
func (h *TodoHandler) List(c *gin.Context) {
	// 解析查询参数
	var priority *models.Priority
	if p := c.Query("priority"); p != "" {
		pr := models.Priority(p)
		priority = &pr
	}

	var completed *bool
	if c.Query("completed") != "" {
		val := c.Query("completed") == "true"
		completed = &val
	}

	// 调用服务层
	todos, err := h.service.ListTodos(priority, completed)
	if err != nil {
		handleError(c, err)
		return
	}

	c.JSON(http.StatusOK, Response{
		Success: true,
		Data:    todos,
	})
}

// Create 创建任务
// POST /api/todos
// @Summary      创建任务
// @Description  创建一个新的待办任务
// @Tags         todos
// @Accept       json
// @Produce      json
// @Param        request  body      service.CreateTodoRequest  true  "创建任务请求"
// @Success      201      {object}  Response{data=models.Todo}
// @Failure      400      {object}  Response
// @Failure      500      {object}  Response
// @Router       /todos [post]
func (h *TodoHandler) Create(c *gin.Context) {
	// 绑定请求体
	var req service.CreateTodoRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusBadRequest,
				Message: "无效的请求体",
			},
		})
		return
	}

	// 调用服务层
	todo, err := h.service.CreateTodo(req)
	if err != nil {
		handleError(c, err)
		return
	}

	c.JSON(http.StatusCreated, Response{
		Success: true,
		Data:    todo,
	})
}

// Get 获取单个任务
// GET /api/todos/:id
// @Summary      获取单个任务
// @Description  根据 ID 获取任务详情
// @Tags         todos
// @Accept       json
// @Produce      json
// @Param        id   path      int  true  "任务 ID"
// @Success      200  {object}  Response{data=models.Todo}
// @Failure      400  {object}  Response
// @Failure      404  {object}  Response
// @Router       /todos/{id} [get]
func (h *TodoHandler) Get(c *gin.Context) {
	// 解析 ID
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusBadRequest,
				Message: "无效的 ID",
			},
		})
		return
	}

	todo, err := h.service.GetTodo(uint(id))
	if err != nil {
		handleError(c, err)
		return
	}

	c.JSON(http.StatusOK, Response{
		Success: true,
		Data:    todo,
	})
}

// Update 更新任务
// PUT /api/todos/:id
// @Summary      更新任务
// @Description  更新指定任务的信息
// @Tags         todos
// @Accept       json
// @Produce      json
// @Param        id       path      int                       true  "任务 ID"
// @Param        request  body      service.UpdateTodoRequest  true  "更新任务请求"
// @Success      200      {object}  Response{data=models.Todo}
// @Failure      400      {object}  Response
// @Failure      404      {object}  Response
// @Router       /todos/{id} [put]
func (h *TodoHandler) Update(c *gin.Context) {
	// 解析 ID
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusBadRequest,
				Message: "无效的 ID",
			},
		})
		return
	}

	// 绑定请求体
	var req service.UpdateTodoRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusBadRequest,
				Message: "无效的请求体",
			},
		})
		return
	}

	todo, err := h.service.UpdateTodo(uint(id), req)
	if err != nil {
		handleError(c, err)
		return
	}

	c.JSON(http.StatusOK, Response{
		Success: true,
		Data:    todo,
	})
}

// Delete 删除任务
// DELETE /api/todos/:id
// @Summary      删除任务
// @Description  删除指定的任务
// @Tags         todos
// @Accept       json
// @Produce      json
// @Param        id   path      int  true  "任务 ID"
// @Success      200  {object}  Response{data=map[string]string}
// @Failure      400  {object}  Response
// @Failure      404  {object}  Response
// @Router       /todos/{id} [delete]
func (h *TodoHandler) Delete(c *gin.Context) {
	// 解析 ID
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, Response{
			Success: false,
			Error: &ErrorInfo{
				Code:    http.StatusBadRequest,
				Message: "无效的 ID",
			},
		})
		return
	}

	if err := h.service.DeleteTodo(uint(id)); err != nil {
		handleError(c, err)
		return
	}

	c.JSON(http.StatusOK, Response{
		Success: true,
		Data: map[string]string{
			"message": "删除成功",
		},
	})
}

// ============================================================
// 中间件（Middleware）
// ------------------------------------------------------------
// Gin 中间件是 gin.HandlerFunc 类型
// 可以在请求到达处理器之前或之后执行代码
// 常用于: 日志、认证、压缩、错误恢复等
// ============================================================

// LoggingMiddleware 日志中间件
func LoggingMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		// 请求前
		path := c.Request.URL.Path
		method := c.Request.Method

		// 调用下一个处理器
		c.Next()

		// 请求后（记录响应状态）
		statusCode := c.Writer.Status()
		latency := c.Writer.Size()
		fmt.Printf("[%s] %s %d %d bytes\n", method, path, statusCode, latency)
	}
}

// RecoveryMiddleware 恢复中间件
// 捕获 panic，防止服务器崩溃
func RecoveryMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		defer func() {
			if err := recover(); err != nil {
				fmt.Printf("Panic recovered: %v\n", err)
				c.AbortWithStatusJSON(http.StatusInternalServerError, Response{
					Success: false,
					Error: &ErrorInfo{
						Code:    http.StatusInternalServerError,
						Message: "服务器内部错误",
					},
				})
			}
		}()
		c.Next()
	}
}

// CORSMiddleware CORS 跨域中间件
func CORSMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
		c.Writer.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(http.StatusOK)
			return
		}

		c.Next()
	}
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

// RegisterRoutes 注册路由
func (h *StatsHandler) RegisterRoutes(r *gin.RouterGroup) {
	r.GET("/stats", h.Get)
}

// Get 获取统计信息
// GET /api/stats
// @Summary      获取统计信息
// @Description  获取任务的统计信息，包括总数、完成数、待办数等
// @Tags         stats
// @Accept       json
// @Produce      json
// @Success      200  {object}  Response{data=service.TodoStats}
// @Failure      500  {object}  Response
// @Router       /stats [get]
func (h *StatsHandler) Get(c *gin.Context) {
	stats, err := h.service.GetStats()
	if err != nil {
		handleError(c, err)
		return
	}

	c.JSON(http.StatusOK, Response{
		Success: true,
		Data:    stats,
	})
}
