package repository

import (
	"todo-list/backend/internal/model"

	"gorm.io/gorm"
)

type TodoRepository struct {
	db *gorm.DB
}

func NewTodoRepository(db *gorm.DB) *TodoRepository {
	return &TodoRepository{db: db}
}

// List returns a paginated list of todos, optionally filtered by status,
// ordered by created_at DESC.
func (r *TodoRepository) List(query *model.QueryTodoReq) ([]model.Todo, int64, error) {
	var todos []model.Todo
	var total int64

	tx := r.db.Model(&model.Todo{})
	if query.Status != nil {
		tx = tx.Where("status = ?", *query.Status)
	}

	if err := tx.Count(&total).Error; err != nil {
		return nil, 0, err
	}

	offset := (query.Page - 1) * query.PageSize
	if err := tx.Order("created_at DESC").
		Offset(offset).
		Limit(query.PageSize).
		Find(&todos).Error; err != nil {
		return nil, 0, err
	}

	return todos, total, nil
}

// GetByID returns a single todo by ID (soft-delete aware).
func (r *TodoRepository) GetByID(id uint) (*model.Todo, error) {
	var todo model.Todo
	if err := r.db.First(&todo, id).Error; err != nil {
		return nil, err
	}
	return &todo, nil
}

// Create inserts a new todo record.
func (r *TodoRepository) Create(todo *model.Todo) error {
	return r.db.Create(todo).Error
}

// Update saves changes to an existing todo record.
func (r *TodoRepository) Update(todo *model.Todo) error {
	return r.db.Save(todo).Error
}

// Delete performs a soft delete on the todo record.
func (r *TodoRepository) Delete(id uint) error {
	return r.db.Delete(&model.Todo{}, id).Error
}
