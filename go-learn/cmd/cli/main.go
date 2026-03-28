// Package main - CLI 命令行工具入口
// ============================================================
// Go CLI 应用程序
// ------------------------------------------------------------
// 使用 Go 标准库 flag 包处理命令行参数
// 演示: 参数解析、文件操作、交互式命令
// ============================================================
package main

import (
	"bufio"
	"encoding/json"
	"flag"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"

	"github.com/hjc0930/go-learn/internal/models"
	"github.com/hjc0930/go-learn/internal/service"
	"github.com/hjc0930/go-learn/internal/storage"
)

// ============================================================
// 全局变量
// ------------------------------------------------------------
// 使用 flag 包定义命令行参数
// flag 类型: String, Int, Bool, Duration 等
// ============================================================

var (
	// 数据文件路径
	dataFile = "todos.json"

	// 服务实例
	todoService *service.TodoService
)

// ============================================================
// main 函数
// ============================================================
func main() {
	// 定义子命令
	// flag.NewFlagSet 创建独立的参数集
	addCmd := flag.NewFlagSet("add", flag.ExitOnError)
	addTitle := addCmd.String("title", "", "任务标题")
	addDesc := addCmd.String("desc", "", "任务描述")
	addPriority := addCmd.String("priority", "medium", "优先级 (low/medium/high)")

	listCmd := flag.NewFlagSet("list", flag.ExitOnError)
	listAll := listCmd.Bool("all", false, "显示所有任务")
	listPriority := listCmd.String("priority", "", "按优先级过滤")

	completeCmd := flag.NewFlagSet("complete", flag.ExitOnError)

	deleteCmd := flag.NewFlagSet("delete", flag.ExitOnError)

	statsCmd := flag.NewFlagSet("stats", flag.ExitOnError)

	interactiveCmd := flag.NewFlagSet("interactive", flag.ExitOnError)

	// 初始化服务
	initService()

	// 检查参数数量
	if len(os.Args) < 2 {
		printUsage()
		os.Exit(1)
	}

	// 根据子命令分发
	switch os.Args[1] {
	case "add", "a":
		addCmd.Parse(os.Args[2:])
		handleAdd(*addTitle, *addDesc, *addPriority)
	case "list", "ls", "l":
		listCmd.Parse(os.Args[2:])
		handleList(*listAll, *listPriority)
	case "complete", "c":
		completeCmd.Parse(os.Args[2:])
		handleComplete(completeCmd.Args())
	case "delete", "d":
		deleteCmd.Parse(os.Args[2:])
		handleDelete(deleteCmd.Args())
	case "stats":
		statsCmd.Parse(os.Args[2:])
		handleStats()
	case "interactive", "i":
		interactiveCmd.Parse(os.Args[2:])
		handleInteractive()
	case "help", "h":
		printUsage()
	case "version", "v":
		fmt.Println("Todo CLI v1.0.0")
	default:
		fmt.Printf("未知命令: %s\n", os.Args[1])
		printUsage()
		os.Exit(1)
	}
}

// initService 初始化服务
func initService() {
	// 尝试使用文件存储
	repo, err := storage.NewFileRepository(dataFile)
	if err != nil {
		// 如果文件不存在，使用内存存储
		fmt.Printf("警告: 无法加载文件 %s，使用内存存储\n", dataFile)
		repo = storage.NewMemoryRepository()
	}
	todoService = service.NewTodoService(repo)
}

// printUsage 打印使用说明
func printUsage() {
	fmt.Println(`Todo CLI - 命令行任务管理工具

用法:
  todo <command> [options]

命令:
  add, a       添加新任务
  list, ls     列出任务
  complete, c  标记任务完成
  delete, d    删除任务
  stats        显示统计信息
  interactive  交互模式

选项:
  add:
    -title string     任务标题
    -desc string      任务描述
    -priority string  优先级 (low/medium/high)

  list:
    -all              显示所有任务（包括已完成）
    -priority string  按优先级过滤

示例:
  todo add -title "学习 Go" -priority high
  todo list
  todo complete 1
  todo delete 1
  todo interactive`)
}

// ============================================================
// 命令处理函数
// ============================================================

// handleAdd 处理添加任务
func handleAdd(title, desc, priority string) {
	if title == "" {
		fmt.Println("错误: 请提供任务标题")
		fmt.Println("用法: todo add -title \"任务标题\" [-desc \"描述\"] [-priority low|medium|high]")
		os.Exit(1)
	}

	// 创建请求
	req := service.CreateTodoRequest{
		Title:       title,
		Description: desc,
		Priority:    models.Priority(priority),
	}

	// 调用服务
	todo, err := todoService.CreateTodo(req)
	if err != nil {
		fmt.Printf("错误: %v\n", err)
		os.Exit(1)
	}

	fmt.Printf("✓ 任务创建成功! ID: %d\n", todo.ID)
}

// handleList 处理列出任务
func handleList(showAll bool, priority string) {
	var todos []*models.Todo
	var err error

	// 根据过滤条件获取任务
	if priority != "" {
		p := models.Priority(priority)
		todos, err = todoService.ListTodos(&p, nil)
	} else if !showAll {
		completed := false
		todos, err = todoService.ListTodos(nil, &completed)
	} else {
		todos, err = todoService.ListTodos(nil, nil)
	}

	if err != nil {
		fmt.Printf("错误: %v\n", err)
		os.Exit(1)
	}

	if len(todos) == 0 {
		fmt.Println("没有任务")
		return
	}

	// 打印表头
	fmt.Println()
	fmt.Printf("%-4s %-8s %-6s %-30s %s\n", "ID", "状态", "优先级", "标题", "创建时间")
	fmt.Println(strings.Repeat("-", 80))

	// 打印任务
	for _, todo := range todos {
		status := " "
		if todo.Completed {
			status = "✓"
		}

		priorityDisplay := string(todo.Priority)
		switch todo.Priority {
		case models.PriorityHigh:
			priorityDisplay = "🔴 high"
		case models.PriorityMedium:
			priorityDisplay = "🟡 medium"
		case models.PriorityLow:
			priorityDisplay = "🟢 low"
		}

		createdAt := todo.CreatedAt.Format("2006-01-02 15:04")

		fmt.Printf("%-4d %-8s %-8s %-30s %s\n",
			todo.ID,
			status,
			priorityDisplay,
			truncate(todo.Title, 30),
			createdAt,
		)
	}

	fmt.Println()
}

// handleComplete 处理完成任务
func handleComplete(args []string) {
	if len(args) == 0 {
		fmt.Println("错误: 请提供任务 ID")
		fmt.Println("用法: todo complete <id>")
		os.Exit(1)
	}

	// 解析 ID
	id, err := strconv.ParseUint(args[0], 10, 64)
	if err != nil {
		fmt.Printf("错误: 无效的 ID '%s'\n", args[0])
		os.Exit(1)
	}

	// 调用服务
	todo, err := todoService.CompleteTodo(uint(id))
	if err != nil {
		fmt.Printf("错误: %v\n", err)
		os.Exit(1)
	}

	fmt.Printf("✓ 任务 '%s' 已完成!\n", todo.Title)
}

// handleDelete 处理删除任务
func handleDelete(args []string) {
	if len(args) == 0 {
		fmt.Println("错误: 请提供任务 ID")
		fmt.Println("用法: todo delete <id>")
		os.Exit(1)
	}

	id, err := strconv.ParseUint(args[0], 10, 64)
	if err != nil {
		fmt.Printf("错误: 无效的 ID '%s'\n", args[0])
		os.Exit(1)
	}

	if err := todoService.DeleteTodo(uint(id)); err != nil {
		fmt.Printf("错误: %v\n", err)
		os.Exit(1)
	}

	fmt.Printf("✓ 任务 ID %d 已删除!\n", id)
}

// handleStats 处理统计信息
func handleStats() {
	stats, err := todoService.GetStats()
	if err != nil {
		fmt.Printf("错误: %v\n", err)
		os.Exit(1)
	}

	fmt.Println()
	fmt.Println("=== 任务统计 ===")
	fmt.Println()
	fmt.Printf("  总任务数: %d\n", stats.Total)
	fmt.Printf("  已完成:   %d\n", stats.Completed)
	fmt.Printf("  待完成:   %d\n", stats.Pending)
	fmt.Println()
	fmt.Println("  按优先级:")
	fmt.Printf("    🔴 高: %d\n", stats.ByPriority[models.PriorityHigh])
	fmt.Printf("    🟡 中: %d\n", stats.ByPriority[models.PriorityMedium])
	fmt.Printf("    🟢 低: %d\n", stats.ByPriority[models.PriorityLow])
	if stats.HighOverdue > 0 {
		fmt.Printf("    ⚠️  高优先级过期: %d\n", stats.HighOverdue)
	}
	fmt.Println()
}

// ============================================================
// 交互模式
// ============================================================

// handleInteractive 处理交互模式
func handleInteractive() {
	reader := bufio.NewReader(os.Stdin)

	fmt.Println()
	fmt.Println("=== Todo 交互模式 ===")
	fmt.Println("输入 'help' 查看可用命令，'exit' 退出")
	fmt.Println()

	for {
		fmt.Print("todo> ")

		// 读取一行输入
		input, err := reader.ReadString('\n')
		if err != nil {
			fmt.Printf("读取错误: %v\n", err)
			continue
		}

		// 去除换行符
		input = strings.TrimSpace(input)
		if input == "" {
			continue
		}

		// 解析命令
		parts := strings.Fields(input)
		cmd := parts[0]
		args := parts[1:]

		switch cmd {
		case "help", "h", "?":
			printInteractiveHelp()
		case "exit", "quit", "q":
			fmt.Println("再见!")
			return
		case "list", "ls", "l":
			handleList(true, "")
		case "add", "a":
			if len(args) < 1 {
				fmt.Println("用法: add <标题> [描述] [优先级]")
				continue
			}
			title := args[0]
			desc := ""
			priority := "medium"
			if len(args) > 1 {
				desc = args[1]
			}
			if len(args) > 2 {
				priority = args[2]
			}
			handleAdd(title, desc, priority)
		case "complete", "c":
			if len(args) < 1 {
				fmt.Println("用法: complete <id>")
				continue
			}
			handleComplete(args)
		case "delete", "d":
			if len(args) < 1 {
				fmt.Println("用法: delete <id>")
				continue
			}
			handleDelete(args)
		case "stats":
			handleStats()
		case "clear":
			// 清屏（跨平台）
			fmt.Print("\033[H\033[2J")
		default:
			fmt.Printf("未知命令: %s。输入 'help' 查看帮助。\n", cmd)
		}
	}
}

// printInteractiveHelp 打印交互模式帮助
func printInteractiveHelp() {
	fmt.Println(`
可用命令:
  list, ls, l          列出所有任务
  add <标题> [描述] [优先级]  添加任务
  complete <id>        标记任务完成
  delete <id>          删除任务
  stats                显示统计
  clear                清屏
  help, h, ?           显示帮助
  exit, quit, q        退出程序
`)
}

// ============================================================
// 辅助函数
// ============================================================

// truncate 截断字符串
func truncate(s string, maxLen int) string {
	if len(s) <= maxLen {
		return s
	}
	return s[:maxLen-3] + "..."
}

// ============================================================
// 文件操作示例（直接操作，不通过服务层）
// ============================================================

// exportTodos 导出任务到 JSON 文件
func exportTodos(filename string) error {
	todos, err := todoService.ListTodos(nil, nil)
	if err != nil {
		return err
	}

	data, err := json.MarshalIndent(todos, "", "  ")
	if err != nil {
		return err
	}

	return os.WriteFile(filename, data, 0644)
}

// importTodos 从 JSON 文件导入任务
func importTodos(filename string) error {
	data, err := os.ReadFile(filename)
	if err != nil {
		return err
	}

	var todos []*models.Todo
	if err := json.Unmarshal(data, &todos); err != nil {
		return err
	}

	for _, todo := range todos {
		req := service.CreateTodoRequest{
			Title:       todo.Title,
			Description: todo.Description,
			Priority:    todo.Priority,
		}
		if _, err := todoService.CreateTodo(req); err != nil {
			fmt.Printf("警告: 无法导入任务 '%s': %v\n", todo.Title, err)
		}
	}

	return nil
}

// ============================================================
// 时间格式化示例
// ------------------------------------------------------------
// Go 使用特定的参考时间来定义格式
// "2006-01-02 15:04:05" 是固定的，不能改
// 记忆方法: 2006年1月2日 15:04:05 (1 2 3 4 5 6)
// ============================================================

func formatTime(t time.Time) string {
	// 使用 Go 的时间格式化
	return t.Format("2006-01-02 15:04:05 Monday")
}
