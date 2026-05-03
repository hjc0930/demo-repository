package handler

import (
	"net/http"
	"strconv"
	"strings"

	"todo-list/backend/internal/model"
	"todo-list/backend/internal/service"
	apperror "todo-list/backend/pkg/apperror"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
)

type TodoHandler struct {
	svc *service.TodoService
}

func NewTodoHandler(svc *service.TodoService) *TodoHandler {
	return &TodoHandler{svc: svc}
}

// RegisterRoutes registers all todo API routes on the given router group.
func (h *TodoHandler) RegisterRoutes(rg *gin.RouterGroup) {
	todos := rg.Group("/todos")
	{
		todos.GET("", h.List)
		todos.GET("/:id", h.GetByID)
		todos.POST("", h.Create)
		todos.PUT("/:id", h.Update)
		todos.DELETE("/:id", h.Delete)
	}
}

// List godoc
// @Summary 获取Todo列表
// @Description 分页获取Todo列表，支持按状态筛选
// @Tags Todo管理
// @Accept json
// @Produce json
// @Param status query int false "状态: 0=待办, 1=进行中, 2=已完成"
// @Param page query int false "页码" default(1)
// @Param pageSize query int false "每页条数" default(10)
// @Success 200 {object} model.Result{data=model.PageResult}
// @Router /api/v1/todos [get]
func (h *TodoHandler) List(c *gin.Context) {
	var query model.QueryTodoReq
	if err := c.ShouldBindQuery(&query); err != nil {
		c.JSON(http.StatusBadRequest, model.Error(400, "参数校验失败"))
		return
	}

	if query.Page <= 0 {
		query.Page = 1
	}
	if query.PageSize <= 0 {
		query.PageSize = 10
	}

	result := h.svc.List(&query)
	c.JSON(http.StatusOK, model.Success(result))
}

// GetByID godoc
// @Summary 获取Todo详情
// @Description 根据ID获取Todo详情
// @Tags Todo管理
// @Accept json
// @Produce json
// @Param id path int true "Todo ID"
// @Success 200 {object} model.Result{data=model.Todo}
// @Failure 404 {object} model.Result
// @Router /api/v1/todos/{id} [get]
func (h *TodoHandler) GetByID(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, model.Error(400, "无效的ID"))
		return
	}

	todo := h.svc.GetByID(uint(id))
	c.JSON(http.StatusOK, model.Success(todo))
}

// Create godoc
// @Summary 创建Todo
// @Description 创建一个新的Todo
// @Tags Todo管理
// @Accept json
// @Produce json
// @Param body body model.CreateTodoReq true "创建Todo请求"
// @Success 200 {object} model.Result{data=model.Todo}
// @Failure 400 {object} model.Result
// @Router /api/v1/todos [post]
func (h *TodoHandler) Create(c *gin.Context) {
	var req model.CreateTodoReq
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, model.Error(400, extractValidationError(err)))
		return
	}

	todo := h.svc.Create(&req)
	c.JSON(http.StatusOK, model.Success(todo))
}

// Update godoc
// @Summary 更新Todo
// @Description 部分更新Todo
// @Tags Todo管理
// @Accept json
// @Produce json
// @Param id path int true "Todo ID"
// @Param body body model.UpdateTodoReq true "更新Todo请求"
// @Success 200 {object} model.Result{data=model.Todo}
// @Failure 400 {object} model.Result
// @Failure 404 {object} model.Result
// @Router /api/v1/todos/{id} [put]
func (h *TodoHandler) Update(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, model.Error(400, "无效的ID"))
		return
	}

	var req model.UpdateTodoReq
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, model.Error(400, extractValidationError(err)))
		return
	}

	todo := h.svc.Update(uint(id), &req)
	c.JSON(http.StatusOK, model.Success(todo))
}

// Delete godoc
// @Summary 删除Todo
// @Description 软删除Todo
// @Tags Todo管理
// @Accept json
// @Produce json
// @Param id path int true "Todo ID"
// @Success 200 {object} model.Result
// @Failure 404 {object} model.Result
// @Router /api/v1/todos/{id} [delete]
func (h *TodoHandler) Delete(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, model.Error(400, "无效的ID"))
		return
	}

	h.svc.Delete(uint(id))
	c.JSON(http.StatusOK, model.SuccessNoData())
}

// RecoveryMiddleware catches panics (especially BusinessError) and returns
// a unified JSON response, matching the Java GlobalExceptionHandler behavior.
func RecoveryMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		defer func() {
			if r := recover(); r != nil {
				switch e := r.(type) {
				case *apperror.BusinessError:
					c.JSON(http.StatusBadRequest, model.Error(e.Code, e.Message))
				default:
					c.JSON(http.StatusInternalServerError, model.Error(500, "服务器内部错误"))
				}
				c.Abort()
			}
		}()
		c.Next()
	}
}

// extractValidationError extracts the first validation error message from a
// binding error, compatible with the Java validation messages.
func extractValidationError(err error) string {
	validationErrs, ok := err.(validator.ValidationErrors)
	if !ok {
		if strings.Contains(err.Error(), "required") {
			return "参数校验失败"
		}
		return "参数校验失败"
	}

	if len(validationErrs) == 0 {
		return "参数校验失败"
	}

	e := validationErrs[0]
	fieldName := e.Field()

	switch e.Tag() {
	case "required":
		return fieldName + "不能为空"
	case "min":
		return fieldName + "不能为空"
	case "max":
		return fieldName + "长度不能超过" + e.Param()
	default:
		return "参数校验失败"
	}
}
