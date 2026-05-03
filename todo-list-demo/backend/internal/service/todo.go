package service

import (
	"strings"

	"todo-list/backend/internal/model"
	"todo-list/backend/internal/repository"
	apperror "todo-list/backend/pkg/apperror"

	"gorm.io/gorm"
)

type TodoService struct {
	repo *repository.TodoRepository
}

func NewTodoService(repo *repository.TodoRepository) *TodoService {
	return &TodoService{repo: repo}
}

// List returns a paginated todo list, optionally filtered by status.
func (s *TodoService) List(query *model.QueryTodoReq) *model.PageResult {
	todos, total, err := s.repo.List(query)
	if err != nil {
		panic(apperror.NewWithCode(500, "查询失败"))
	}
	return &model.PageResult{
		List:     todos,
		Total:    total,
		Page:     query.Page,
		PageSize: query.PageSize,
	}
}

// GetByID returns a single todo or panics with a 404 error.
func (s *TodoService) GetByID(id uint) *model.Todo {
	todo, err := s.repo.GetByID(id)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			panic(apperror.NewWithCode(404, "Todo不存在"))
		}
		panic(apperror.NewWithCode(500, "查询失败"))
	}
	return todo
}

// Create builds a Todo from the request and persists it.
func (s *TodoService) Create(req *model.CreateTodoReq) *model.Todo {
	todo := &model.Todo{
		Title:       req.Title,
		Description: req.Description,
		Status:      req.Status,
		Priority:    req.Priority,
		DueDate:     req.DueDate,
	}
	if todo.Status == 0 {
		todo.Status = 0
	}
	if todo.Priority == 0 {
		todo.Priority = 1
	}
	if err := s.repo.Create(todo); err != nil {
		panic(apperror.NewWithCode(500, "创建失败"))
	}
	return todo
}

// Update applies partial updates from the request to an existing todo.
func (s *TodoService) Update(id uint, req *model.UpdateTodoReq) *model.Todo {
	todo := s.GetByID(id)

	if req.Title != nil && strings.TrimSpace(*req.Title) != "" {
		todo.Title = *req.Title
	}
	if req.Description != nil {
		todo.Description = *req.Description
	}
	if req.Status != nil {
		todo.Status = *req.Status
	}
	if req.Priority != nil {
		todo.Priority = *req.Priority
	}
	if req.DueDate != nil {
		todo.DueDate = req.DueDate
	}

	if err := s.repo.Update(todo); err != nil {
		panic(apperror.NewWithCode(500, "更新失败"))
	}
	return todo
}

// Delete soft-deletes a todo by ID.
func (s *TodoService) Delete(id uint) {
	s.GetByID(id) // ensure exists
	if err := s.repo.Delete(id); err != nil {
		panic(apperror.NewWithCode(500, "删除失败"))
	}
}
