package main

import (
	"go-crud-demo/src/config"
	"go-crud-demo/src/entitys"
	"go-crud-demo/src/vo"

	"github.com/gin-gonic/gin"
)

func main() {
	// Connect to database
	db, _ := config.CreateSchema()
	db.AutoMigrate(&entitys.Student{})

	// Created service
	ginService := gin.Default()
	PORT := "8080"

	ginService.GET("/", func(ctx *gin.Context) {
		ctx.JSON(200, gin.H{
			"message": "Successful!",
		})
	})
	ginService.GET("/list", func(ctx *gin.Context) {
		var dataList vo.StudentVo

		db.Model(&dataList.List).Count(&dataList.Total).Find(&dataList.List)
		ctx.JSON(200, gin.H{
			"code":    200,
			"data":    dataList,
			"message": "查询成功",
		})

	})

	ginService.Run(":" + PORT)
}
