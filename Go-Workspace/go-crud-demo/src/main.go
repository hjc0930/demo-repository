package main

import (
	"go-crud-demo/src/config"
	"go-crud-demo/src/entitys"

	"github.com/gin-gonic/gin"
)

func main() {
	// Connect to database
	db, _ := config.CreateSchema()
	db.AutoMigrate(&entitys.List{})

	// Created service
	config.GinService = gin.Default()
	PORT := "8080"

	config.GinService.Run(":" + PORT)
}
