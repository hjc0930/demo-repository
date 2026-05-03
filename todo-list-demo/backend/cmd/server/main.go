package main

import (
	"fmt"
	"log"

	"todo-list/backend/docs"
	"todo-list/backend/internal/config"
	"todo-list/backend/internal/handler"
	"todo-list/backend/internal/model"
	"todo-list/backend/internal/repository"
	"todo-list/backend/internal/service"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"gorm.io/driver/mysql"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

// @title Todo API
// @version 1.0
// @description Todo 管理系统 API
// @host localhost:8080
// @BasePath /api/v1
func main() {
	cfg := config.Load()

	// Connect to database
	var db *gorm.DB
	var err error
	switch cfg.DB.Driver {
	case "mysql":
		db, err = gorm.Open(mysql.Open(cfg.DB.DSN()), &gorm.Config{
			Logger: logger.Default.LogMode(logger.Info),
		})
	default:
		db, err = gorm.Open(sqlite.Open(cfg.DB.SQLiteDSN()), &gorm.Config{
			Logger: logger.Default.LogMode(logger.Info),
		})
	}
	if err != nil {
		log.Fatalf("failed to connect database: %v", err)
	}

	// Auto migrate (creates table if not exists)
	if err := db.AutoMigrate(&model.Todo{}); err != nil {
		log.Fatalf("failed to auto migrate: %v", err)
	}

	// Wire dependencies
	repo := repository.NewTodoRepository(db)
	svc := service.NewTodoService(repo)
	h := handler.NewTodoHandler(svc)

	// Setup Gin
	r := gin.Default()
	r.Use(handler.RecoveryMiddleware())

	// CORS
	r.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"*"},
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Type", "Authorization"},
		AllowCredentials: true,
	}))

	// API routes
	apiV1 := r.Group("/api/v1")
	h.RegisterRoutes(apiV1)

	// Swagger
	docs.SwaggerInfo.BasePath = "/api/v1"
	r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	addr := ":" + cfg.Port
	fmt.Println("Server starting on", addr)
	if err := r.Run(addr); err != nil {
		log.Fatalf("failed to start server: %v", err)
	}
}
