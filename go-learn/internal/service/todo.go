// Package service 提供业务逻辑层
// ============================================================
// 服务层（Service Layer）
// ------------------------------------------------------------
// 服务层位于存储层和处理器之间，负责:
// 1. 实现业务逻辑
// 2. 数据验证
// 3. 协调多个存储操作
// 4. 事务管理
//
// 这种分层架构使代码更易于测试和维护
// ============================================================
package service

import (
	"errors"
	"fmt"
	"strings"
	"time"

	"github.com/hjc0930/go-learn/internal/models"
	"github.com/hjc0930/go-learn/internal/storage"
)

// ============================================================
// 自定义错误类型
// ------------------------------------------------------------
// 实现 error 接口的自定义类型
// 可以携带更多上下文信息
// ============================================================

// ValidationError 表示验证错误
type ValidationError struct {
	Field   string // 出错的字段名
	Message string // 错误消息
}

// Error 实现 error 接口
func (e *ValidationError) Error() string {
	return fmt.Sprintf("validation error: %s - %s", e.Field, e.Message)
}

// BusinessError 表示业务逻辑错误
type BusinessError struct {
	Code    int
	Message string
}

func (e *BusinessError) Error() string {
	return fmt.Sprintf("business error [%d]: %s", e.Code, e.Message)
}

// ============================================================
// TodoService 提供任务相关的业务逻辑
// ------------------------------------------------------------
// 服务依赖存储接口，而不是具体实现
// 这是依赖倒置原则（DIP）的应用
// ============================================================
type TodoService struct {
	repo storage.Repository
}

// NewTodoService 创建服务实例
// 接收接口类型，可以传入任何实现了 Repository 的类型
func NewTodoService(repo storage.Repository) *TodoService {
	return &TodoService{
		repo: repo,
	}
}

// CreateTodoRequest 创建任务的请求参数
// 使用单独的结构体来定义请求，可以:
// 1. 分离 API 层和内部模型
// 2. 添加验证标签
// 3. 控制哪些字段可以被外部设置
type CreateTodoRequest struct {
	Title       string          `json:"title"`
	Description string          `json:"description"`
	Priority    models.Priority `json:"priority"`
	Tags        []string        `json:"tags"`
}

// UpdateTodoRequest 更新任务的请求参数
type UpdateTodoRequest struct {
	Title       *string          `json:"title,omitempty"`       // 使用指针区分"不更新"和"更新为零值"
	Description *string          `json:"description,omitempty"`
	Completed   *bool            `json:"completed,omitempty"`
	Priority    *models.Priority `json:"priority,omitempty"`
	Tags        []string         `json:"tags,omitempty"`
}

// CreateTodo 创建新任务
// 包含业务验证逻辑
func (s *TodoService) CreateTodo(req CreateTodoRequest) (*models.Todo, error) {
	// 验证逻辑
	if err := s.validateCreateRequest(req); err != nil {
		return nil, err
	}

	// 创建任务模型
	todo := models.NewTodo(req.Title, req.Description, req.Priority)

	// 设置标签
	if len(req.Tags) > 0 {
		todo.Tags = s.normalizeTags(req.Tags)
	}

	// 调用存储层保存
	if err := s.repo.Create(todo); err != nil {
		return nil, fmt.Errorf("failed to create todo: %w", err)
	}

	return todo, nil
}

// GetTodo 获取任务详情
func (s *TodoService) GetTodo(id uint) (*models.Todo, error) {
	if id == 0 {
		return nil, &ValidationError{
			Field:   "id",
			Message: "id must be greater than 0",
		}
	}

	todo, err := s.repo.GetByID(id)
	if err != nil {
		if errors.Is(err, storage.ErrNotFound) {
			return nil, &BusinessError{
				Code:    404,
				Message: fmt.Sprintf("todo with id %d not found", id),
			}
		}
		return nil, fmt.Errorf("failed to get todo: %w", err)
	}

	return todo, nil
}

// ListTodos 获取任务列表
// 支持按优先级过滤
func (s *TodoService) ListTodos(priority *models.Priority, completed *bool) ([]*models.Todo, error) {
	// 如果有过滤条件，使用特定查询
	if priority != nil {
		return s.repo.FindByPriority(*priority)
	}

	if completed != nil && *completed {
		return s.repo.FindCompleted()
	}

	// 否则返回所有
	return s.repo.GetAll()
}

// UpdateTodo 更新任务
func (s *TodoService) UpdateTodo(id uint, req UpdateTodoRequest) (*models.Todo, error) {
	// 先获取现有任务
	todo, err := s.GetTodo(id)
	if err != nil {
		return nil, err
	}

	// 更新字段（只更新提供的字段）
	if req.Title != nil {
		if *req.Title == "" {
			return nil, &ValidationError{
				Field:   "title",
				Message: "title cannot be empty",
			}
		}
		todo.Title = *req.Title
	}

	if req.Description != nil {
		todo.Description = *req.Description
	}

	if req.Completed != nil {
		if *req.Completed {
			todo.Complete()
		} else {
			todo.Uncomplete()
		}
	}

	if req.Priority != nil {
		if !s.isValidPriority(*req.Priority) {
			return nil, &ValidationError{
				Field:   "priority",
				Message: "invalid priority value",
			}
		}
		todo.Priority = *req.Priority
	}

	if req.Tags != nil {
		todo.Tags = s.normalizeTags(req.Tags)
	}

	// 更新时间戳
	todo.UpdatedAt = time.Now()

	// 保存更新
	if err := s.repo.Update(todo); err != nil {
		return nil, fmt.Errorf("failed to update todo: %w", err)
	}

	return todo, nil
}

// DeleteTodo 删除任务
func (s *TodoService) DeleteTodo(id uint) error {
	// 先检查是否存在
	if _, err := s.GetTodo(id); err != nil {
		return err
	}

	return s.repo.Delete(id)
}

// CompleteTodo 标记任务为完成
func (s *TodoService) CompleteTodo(id uint) (*models.Todo, error) {
	todo, err := s.GetTodo(id)
	if err != nil {
		return nil, err
	}

	todo.Complete()

	if err := s.repo.Update(todo); err != nil {
		return nil, fmt.Errorf("failed to complete todo: %w", err)
	}

	return todo, nil
}

// ============================================================
// 私有辅助方法
// ============================================================

// validateCreateRequest 验证创建请求
func (s *TodoService) validateCreateRequest(req CreateTodoRequest) error {
	// 验证标题
	if strings.TrimSpace(req.Title) == "" {
		return &ValidationError{
			Field:   "title",
			Message: "title is required",
		}
	}

	if len(req.Title) > 200 {
		return &ValidationError{
			Field:   "title",
			Message: "title cannot exceed 200 characters",
		}
	}

	// 验证优先级
	if !s.isValidPriority(req.Priority) {
		return &ValidationError{
			Field:   "priority",
			Message: "invalid priority, must be low, medium, or high",
		}
	}

	return nil
}

// isValidPriority 检查优先级是否有效
func (s *TodoService) isValidPriority(p models.Priority) bool {
	switch p {
	case models.PriorityLow, models.PriorityMedium, models.PriorityHigh:
		return true
	default:
		return false
	}
}

// normalizeTags 规范化标签
func (s *TodoService) normalizeTags(tags []string) []string {
	result := make([]string, 0, len(tags))
	seen := make(map[string]bool)

	for _, tag := range tags {
		// 去除空格并转小写
		tag = strings.ToLower(strings.TrimSpace(tag))
		if tag != "" && !seen[tag] {
			seen[tag] = true
			result = append(result, tag)
		}
	}

	return result
}

// ============================================================
// 批量操作（演示并发处理）
// ------------------------------------------------------------

// BatchCreateResult 批量创建的结果
type BatchCreateResult struct {
	Success []*models.Todo
	Failed  []BatchCreateError
}

// BatchCreateError 批量创建的错误
type BatchCreateError struct {
	Index int
	Error error
}

// BatchCreate 批量创建任务
// 注意: 这个方法演示了简单的批量操作
// 更复杂的并发版本在 pkg/concurrency 包中
func (s *TodoService) BatchCreate(requests []CreateTodoRequest) *BatchCreateResult {
	result := &BatchCreateResult{
		Success: make([]*models.Todo, 0),
		Failed:  make([]BatchCreateError, 0),
	}

	for i, req := range requests {
		todo, err := s.CreateTodo(req)
		if err != nil {
			result.Failed = append(result.Failed, BatchCreateError{
				Index: i,
				Error: err,
			})
		} else {
			result.Success = append(result.Success, todo)
		}
	}

	return result
}

// ============================================================
// 统计方法
// ------------------------------------------------------------

// TodoStats 任务统计
type TodoStats struct {
	Total       int
	Completed   int
	Pending     int
	ByPriority  map[models.Priority]int
	HighOverdue int // 高优先级过期任务数
}

// GetStats 获取任务统计
func (s *TodoService) GetStats() (*TodoStats, error) {
	todos, err := s.repo.GetAll()
	if err != nil {
		return nil, err
	}

	stats := &TodoStats{
		ByPriority: make(map[models.Priority]int),
	}

	for _, todo := range todos {
		stats.Total++

		if todo.Completed {
			stats.Completed++
		} else {
			stats.Pending++
		}

		stats.ByPriority[todo.Priority]++

		if todo.IsOverdue() {
			stats.HighOverdue++
		}
	}

	return stats, nil
}
