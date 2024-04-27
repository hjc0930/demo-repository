package entitys

import "gorm.io/gorm"

type Student struct {
	gorm.Model
	Name   string `gorm:"type:varchar(20); not null" json:"name" binding:"required"`
	Gender string `gorm:"type:varchar(20); not null" json:"gender" binding:"required"`
	Age    int    `gorm:"type:int; not null" json:"age" binding:"required"`
	Class  string `gorm:"type:varchar(20); not null" json:"class" binding:"required"`
	Score  int    `gorm:"type:int; not null" json:"score" binding:"required"`
}
