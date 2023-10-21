package controllers

import (
	"go-crud-demo/src/config"
	"go-crud-demo/src/entitys"
	"go-crud-demo/src/vo"
	"strconv"

	"github.com/gin-gonic/gin"
)

func ListController() {
	config.GinService.GET("/", func(ctx *gin.Context) {
		ctx.JSON(200, gin.H{
			"message": "Successful!",
		})
	})

	config.GinService.POST("/list", func(ctx *gin.Context) {
		var data entitys.List

		err := ctx.ShouldBindJSON(&data)

		if err != nil {
			ctx.JSON(400, gin.H{
				"code":    400,
				"message": "Create faild",
			})
		} else {
			// Add data to schema
			config.Db.Create(&data)

			ctx.JSON(200, gin.H{
				"code":    200,
				"data":    data.ID,
				"message": "Create Succellful",
			})
		}
	})

	config.GinService.DELETE("/list/:id", func(ctx *gin.Context) {
		var data []entitys.List

		id := ctx.Param("id")

		config.Db.Where("id = ?", id).Find(&data)

		if len(data) == 0 {
			ctx.JSON(400, gin.H{
				"code":    400,
				"message": "ID未找到",
			})
		} else {
			config.Db.Where("id = ?", id).Delete(&data)

			ctx.JSON(200, gin.H{
				"code":    200,
				"message": "删除成功",
			})
		}

	})

	config.GinService.PUT("/list/:id", func(ctx *gin.Context) {
		var data entitys.List

		id := ctx.Param("id")

		err := ctx.ShouldBindJSON(&data)
		config.Db.Select("id").Where("id = ?", id).Find(&data)

		if err != nil {
			ctx.JSON(400, gin.H{
				"code":    400,
				"message": "修改失败",
			})
			return
		}
		if data.ID == 0 || data.DeletedAt.Time.Hour() != 0 {
			ctx.JSON(400, gin.H{
				"code":    400,
				"data":    data,
				"message": "ID未找到",
			})
			return
		}

		config.Db.Where("id = ?", id).Updates(&data)

		ctx.JSON(200, gin.H{
			"code":    200,
			"message": "修改成功",
		})
	})

	config.GinService.GET("/list", func(ctx *gin.Context) {
		var dataList vo.ListVo

		config.Db.Model(&dataList.List).Order("created_at DESC").Count(&dataList.Total).Find(&dataList.List)

		ctx.JSON(200, gin.H{
			"code":    200,
			"data":    dataList,
			"message": "查询成功",
		})

	})

	config.GinService.GET("/list/page", func(ctx *gin.Context) {
		page, pageErr := strconv.Atoi(ctx.Query("page"))
		size, sizeErr := strconv.Atoi(ctx.Query("size"))

		if pageErr != nil || sizeErr != nil {
			ctx.JSON(200, gin.H{
				"code":    400,
				"message": "参数错误",
			})
			return
		}

		var dataList vo.ListVo

		config.Db.Model(&dataList.List).Order("created_at DESC").Count(&dataList.Total).Limit(size).Offset(page).Find(&dataList.List)

		ctx.JSON(200, gin.H{
			"code":    200,
			"data":    dataList,
			"message": "查询成功",
		})
	})

}
