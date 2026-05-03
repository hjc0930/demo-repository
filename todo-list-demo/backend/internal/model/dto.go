package model

import "time"

// CreateTodoReq is the request body for creating a todo.
type CreateTodoReq struct {
	Title       string     `json:"title" binding:"required,min=1,max=255"`
	Description string     `json:"description" binding:"max=2000"`
	Status      int8       `json:"status"`
	Priority    int8       `json:"priority"`
	DueDate     *time.Time `json:"dueDate"`
}

// UpdateTodoReq is the request body for updating a todo (partial update).
type UpdateTodoReq struct {
	Title       *string    `json:"title" binding:"omitempty,max=255"`
	Description *string    `json:"description" binding:"omitempty,max=2000"`
	Status      *int8      `json:"status"`
	Priority    *int8      `json:"priority"`
	DueDate     *time.Time `json:"dueDate"`
}

// QueryTodoReq is the query parameters for listing todos.
type QueryTodoReq struct {
	Status   *int8 `form:"status"`
	Page     int   `form:"page,default=1"`
	PageSize int   `form:"pageSize,default=10"`
}
