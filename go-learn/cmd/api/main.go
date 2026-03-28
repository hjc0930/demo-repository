// Package main - HTTP API 服务器入口
// ============================================================
// Go Web 服务器
// ------------------------------------------------------------
// 使用 Go 标准库 net/http 构建简单的 REST API
// 涵盖: 路由、中间件、JSON 处理、错误处理
// ============================================================
package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

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

	// 初始化依赖
	// 使用依赖注入模式，从底层到顶层依次初始化
	repo := storage.NewMemoryRepository()      // 存储层
	svc := service.NewTodoService(repo)        // 服务层
	todoHandler := handler.NewTodoHandler(svc) // 处理器层
	statsHandler := handler.NewStatsHandler(svc)

	// 创建路由器
	// http.ServeMux 是 Go 标准库提供的 HTTP 路由器
	mux := http.NewServeMux()

	// 注册路由
	// HandleFunc 将路径注册到处理函数
	// Handle 将路径注册到实现了 http.Handler 接口的对象
	mux.HandleFunc("/", homeHandler)
	mux.Handle("/api/todos", todoHandler)      // 处理 /api/todos
	mux.Handle("/api/todos/", todoHandler)     // 处理 /api/todos/{id}
	mux.Handle("/api/stats", statsHandler)     // 处理 /api/stats

	// 应用中间件
	// 中间件按顺序执行：Recovery -> Logging -> Handler
	var finalHandler http.Handler = mux
	finalHandler = handler.LoggingMiddleware(finalHandler)
	finalHandler = handler.RecoveryMiddleware(finalHandler)

	// 创建 HTTP 服务器
	// http.Server 提供了更多配置选项
	server := &http.Server{
		Addr:         ":8080", // 监听地址
		Handler:      finalHandler,
		ReadTimeout:  10 * time.Second,  // 读取超时
		WriteTimeout: 10 * time.Second,  // 写入超时
		IdleTimeout:  60 * time.Second,  // 空闲超时
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
		fmt.Println("API 文档:")
		fmt.Println("  GET    /api/todos          获取任务列表")
		fmt.Println("  POST   /api/todos          创建任务")
		fmt.Println("  GET    /api/todos/{id}     获取单个任务")
		fmt.Println("  PUT    /api/todos/{id}     更新任务")
		fmt.Println("  DELETE /api/todos/{id}     删除任务")
		fmt.Println("  GET    /api/stats          获取统计信息")
		fmt.Println()
		fmt.Println("按 Ctrl+C 停止服务器")

		// ListenAndServe 启动 HTTP 服务器
		// 它会阻塞，直到服务器出错或关闭
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("服务器错误: %v", err)
		}
	}()

	// 等待中断信号
	// os.Signal 类型的 channel 用于接收系统信号
	quit := make(chan os.Signal, 1)
	// signal.Notify 注册要监听的信号
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// 阻塞等待信号
	<-quit
	fmt.Println("\n正在关闭服务器...")

	// 给正在处理的请求一些时间完成
	// context.Background() 返回一个空的 context
	// context.WithTimeout 创建一个带超时的 context
	// server.Shutdown 优雅关闭，等待所有连接处理完成
	// 超时后强制关闭
	// ... 这里简化处理

	fmt.Println("服务器已关闭")
}

// homeHandler 处理首页请求
func homeHandler(w http.ResponseWriter, r *http.Request) {
	// 只处理根路径
	if r.URL.Path != "/" {
		http.NotFound(w, r)
		return
	}

	w.Header().Set("Content-Type", "text/html; charset=utf-8")
	fmt.Fprintln(w, `<!DOCTYPE html>
<html>
<head>
    <title>Go Todo API</title>
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
    <h1>Go Todo API</h1>
    <p>欢迎使用 Go Todo API 服务！</p>

    <h2>API 端点</h2>
    <div class="endpoint">
        <span class="method get">GET</span> <code>/api/todos</code> - 获取任务列表
    </div>
    <div class="endpoint">
        <span class="method post">POST</span> <code>/api/todos</code> - 创建任务
    </div>
    <div class="endpoint">
        <span class="method get">GET</span> <code>/api/todos/{id}</code> - 获取单个任务
    </div>
    <div class="endpoint">
        <span class="method put">PUT</span> <code>/api/todos/{id}</code> - 更新任务
    </div>
    <div class="endpoint">
        <span class="method delete">DELETE</span> <code>/api/todos/{id}</code> - 删除任务
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

  一个学习 Go 语言的示例项目
`)
}
