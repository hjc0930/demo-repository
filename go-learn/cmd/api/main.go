// Package main - HTTP API 服务器入口
// ============================================================
// Go Web 服务器
// ------------------------------------------------------------
// 使用 Gin 框架构建 REST API
// 涵盖: 路由、中间件、JSON 处理、错误处理、优雅关闭
// ============================================================
package main

// @title           Go Todo API
// @version         1.0
// @description     一个基于 Gin 框架的 Todo API 示例项目，用于学习 Go 语言 Web 开发。
// @termsOfService  http://swagger.io/terms/

// @contact.name   API Support
// @contact.url    http://www.swagger.io/support
// @contact.email  support@swagger.io

// @license.name  Apache 2.0
// @license.url   http://www.apache.org/licenses/LICENSE-2.0.html

// @host      localhost:8080
// @BasePath  /api
// @schemes   http

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"

	_ "github.com/hjc0930/go-learn/docs" // swagger 生成的文档
	"github.com/hjc0930/go-learn/internal/handler"
	"github.com/hjc0930/go-learn/internal/service"
	"github.com/hjc0930/go-learn/internal/storage"
)

// ============================================================
// main 函数 - 程序入口
// ------------------------------------------------------------
// Go 程序从 main 包的 main 函数开始执行
// main 函数不接受参数，不返回值
// 命令行参数通过 os.Args 获取
// ============================================================
func main() {
	// 打印欢迎信息
	printBanner()

	// 设置 Gin 运行模式
	// gin.DebugMode / gin.ReleaseMode / gin.TestMode
	gin.SetMode(gin.DebugMode)

	// 初始化依赖
	// 使用依赖注入模式，从底层到顶层依次初始化
	repo := storage.NewMemoryRepository()      // 存储层
	svc := service.NewTodoService(repo)        // 服务层
	todoHandler := handler.NewTodoHandler(svc) // 处理器层
	statsHandler := handler.NewStatsHandler(svc)

	// 创建 Gin 引擎
	// gin.New() 创建一个不带默认中间件的引擎
	// gin.Default() 创建一个带 Logger 和 Recovery 中间件的引擎
	router := gin.New()

	// 注册全局中间件
	router.Use(handler.RecoveryMiddleware()) // 恢复中间件（自定义）
	router.Use(handler.CORSMiddleware())     // CORS 中间件
	router.Use(gin.Logger())                 // Gin 日志中间件

	// 注册首页路由
	router.GET("/", homeHandler)

	// 注册 API 路由组
	api := router.Group("/api")
	{
		todoHandler.RegisterRoutes(api)
		statsHandler.RegisterRoutes(api)
	}

	// 注册 Swagger 文档路由
	// 访问 http://localhost:8080/swagger/index.html 查看 API 文档
	router.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	// 创建 HTTP 服务器
	server := &http.Server{
		Addr:         ":8080",
		Handler:      router,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// ============================================================
	// 优雅关闭（Graceful Shutdown）
	// ------------------------------------------------------------
	// 监听系统信号，在收到中断信号时优雅关闭服务器
	// 常见信号:
	// - SIGINT: Ctrl+C
	// - SIGTERM: kill 命令
	// ============================================================

	// 使用 goroutine 启动服务器
	go func() {
		fmt.Println("服务器启动在 http://localhost:8080")
		fmt.Println("Swagger 文档: http://localhost:8080/swagger/index.html")
		fmt.Println()
		fmt.Println("API 端点:")
		fmt.Println("  GET    /api/todos          获取任务列表")
		fmt.Println("  POST   /api/todos          创建任务")
		fmt.Println("  GET    /api/todos/:id      获取单个任务")
		fmt.Println("  PUT    /api/todos/:id      更新任务")
		fmt.Println("  DELETE /api/todos/:id      删除任务")
		fmt.Println("  GET    /api/stats          获取统计信息")
		fmt.Println()
		fmt.Println("按 Ctrl+C 停止服务器")

		// ListenAndServe 启动 HTTP 服务器
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("服务器错误: %v", err)
		}
	}()

	// 等待中断信号
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// 阻塞等待信号
	<-quit
	fmt.Println("\n正在关闭服务器...")

	// 优雅关闭
	// 给正在处理的请求 5 秒时间完成
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := server.Shutdown(ctx); err != nil {
		log.Printf("服务器关闭错误: %v", err)
	}

	fmt.Println("服务器已关闭")
}

// homeHandler 处理首页请求
func homeHandler(c *gin.Context) {
	c.Header("Content-Type", "text/html; charset=utf-8")
	c.String(http.StatusOK, `<!DOCTYPE html>
<html>
<head>
    <title>Go Todo API (Gin)</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
        h1 { color: #333; }
        code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; }
        pre { background: #f4f4f4; padding: 15px; border-radius: 5px; overflow-x: auto; }
        .endpoint { margin: 10px 0; }
        .method { color: #fff; padding: 3px 8px; border-radius: 3px; font-weight: bold; }
        .get { background: #61affe; }
        .post { background: #49cc90; }
        .put { background: #fca130; }
        .delete { background: #f93e3e; }
    </style>
</head>
<body>
    <h1>🚀 Go Todo API (Gin)</h1>
    <p>欢迎使用基于 Gin 框架的 Todo API 服务！</p>

    <h2>API 端点</h2>
    <div class="endpoint">
        <span class="method get">GET</span> <code>/api/todos</code> - 获取任务列表
    </div>
    <div class="endpoint">
        <span class="method post">POST</span> <code>/api/todos</code> - 创建任务
    </div>
    <div class="endpoint">
        <span class="method get">GET</span> <code>/api/todos/:id</code> - 获取单个任务
    </div>
    <div class="endpoint">
        <span class="method put">PUT</span> <code>/api/todos/:id</code> - 更新任务
    </div>
    <div class="endpoint">
        <span class="method delete">DELETE</span> <code>/api/todos/:id</code> - 删除任务
    </div>
    <div class="endpoint">
        <span class="method get">GET</span> <code>/api/stats</code> - 获取统计信息
    </div>

    <h2>创建任务示例</h2>
    <pre>
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"学习 Go","priority":"high"}'
    </pre>
</body>
</html>`)
}

// printBanner 打印欢迎横幅
func printBanner() {
	fmt.Println(`
   ____       _       _
  / ___| ___ (_)_ __ | |__
 | |  _ / _ \| | '_ \| '_ \
 | |_| | (_) | | | | | |_) |
  \____|\___/|_|_| |_|_.__/

  一个学习 Go 语言的示例项目 (Gin 版本)
`)
}
