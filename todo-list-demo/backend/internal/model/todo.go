package model

import (
	"time"

	"gorm.io/gorm"
)

// Todo is the GORM model mapped to the "todos" table.
type Todo struct {
	ID          uint           `gorm:"primaryKey;autoIncrement" json:"id"`
	Title       string         `gorm:"type:varchar(255);not null" json:"title"`
	Description string         `gorm:"type:text;default:''" json:"description"`
	Status      int8           `gorm:"type:tinyint;not null;default:0" json:"status"`
	Priority    int8           `gorm:"type:tinyint;not null;default:1" json:"priority"`
	DueDate     *time.Time     `gorm:"index" json:"dueDate"`
	CreatedAt   time.Time      `gorm:"autoCreateTime" json:"createdAt"`
	UpdatedAt   time.Time      `gorm:"autoUpdateTime" json:"updatedAt"`
	DeletedAt   gorm.DeletedAt `gorm:"index" json:"-"`
}

// TableName specifies the table name for GORM.
func (Todo) TableName() string {
	return "todos"
}
