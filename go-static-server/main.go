package main

import (
	"log"
	"net/http"
	"os"
	"path/filepath"
)

// Custom handler，adapted to front-end history routing
func staticFileHandler(staticDir string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// 记录请求信息
		log.Printf("[请求] %s %s", r.Method, r.URL.Path)
		// 拼接请求路径对应的文件路径
		filePath := filepath.Join(staticDir, r.URL.Path)
		// 检查文件是否存在
		if _, err := os.Stat(filePath); os.IsNotExist(err) {
			// 文件不存在则返回index.html（SPA路由回退）
			log.Printf("[回退] 文件不存在: %s, 返回 index.html (SPA路由)", r.URL.Path)
			http.ServeFile(w, r, filepath.Join(staticDir, "index.html"))
			return
		} else if err != nil {
			// 其他错误则返回500
			log.Printf("[错误] 检查文件失败: %s, 错误: %v", filePath, err)
			http.Error(w, "Internal Server Error", http.StatusInternalServerError)
			return
		}

		// 文件存在则正常提供服务
		log.Printf("[成功] 返回静态文件: %s", r.URL.Path)
		http.FileServer(http.Dir(staticDir)).ServeHTTP(w, r)
	}
}

func main() {
	statisDir := "./statis"

	absStaticDir, err := filepath.Abs(statisDir)

	if err != nil {
		log.Fatalf("获取静态资源目录绝对路径失败: %v", err)
	}

	// 检查静态资源目录是否存在
	if _, err := os.Stat(absStaticDir); os.IsNotExist(err) {
		log.Fatalf("静态资源目录不存在: %s", absStaticDir)
	}

	log.Printf("[启动] 静态资源目录: %s", absStaticDir)

	// 注册自定义处理器（适配 History 路由）
	http.HandleFunc("/", staticFileHandler(absStaticDir))

	// 启动服务器
	port := ":8080"
	log.Printf("[启动] 服务器启动中，监听端口: %s", port)
	log.Printf("[启动] 访问地址: http://localhost%s", port)
	if err := http.ListenAndServe(port, nil); err != nil {
		log.Fatalf("服务器启动失败: %v", err)
	}
}
