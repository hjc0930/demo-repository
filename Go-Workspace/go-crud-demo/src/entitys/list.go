package entitys

import "gorm.io/gorm"

type List struct {
	gorm.Model
	Name    string `gorm:"type:varchar(20); not null" json:"name" binding:"required"`
	State   string `gorm:"type:varchar(20); not null" json:"state" binding:"required"`
	Phone   string `gorm:"type:varchar(20); not null" json:"phone" binding:"required"`
	Email   string `gorm:"type:varchar(20); not null" json:"email" binding:"required"`
	Address string `gorm:"type:varchar(20); not null" json:"address" binding:"required"`
}
