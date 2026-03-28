// Package main - 并发示例运行入口
package main

import (
	"fmt"
	"os"
	"strconv"

	"github.com/hjc0930/go-learn/pkg/concurrency"
)

func main() {
	fmt.Println("Go 并发编程示例")
	fmt.Println("================")
	fmt.Println()
	fmt.Println("可用示例:")
	fmt.Println("  1 - Goroutine 基础")
	fmt.Println("  2 - WaitGroup")
	fmt.Println("  3 - Mutex 互斥锁")
	fmt.Println("  4 - Channel 基础")
	fmt.Println("  5 - 有缓冲 Channel")
	fmt.Println("  6 - 定向 Channel")
	fmt.Println("  7 - Select 多路复用")
	fmt.Println("  8 - Select 超时")
	fmt.Println("  9 - 非阻塞 Select")
	fmt.Println("  10 - Context")
	fmt.Println("  11 - Context 传值")
	fmt.Println("  12 - Worker Pool")
	fmt.Println("  13 - Pipeline")
	fmt.Println("  14 - Fan-Out Fan-In")
	fmt.Println("  all - 运行所有示例")
	fmt.Println("  q - 退出")
	fmt.Println()

	for {
		fmt.Print("请选择 (1-14/all/q): ")
		var input string
		fmt.Scanln(&input)

		if input == "q" || input == "quit" || input == "exit" {
			fmt.Println("再见!")
			break
		}

		if input == "all" {
			concurrency.RunAllDemos()
			continue
		}

		num, err := strconv.Atoi(input)
		if err != nil {
			fmt.Println("无效输入，请输入数字或 'all'")
			continue
		}

		switch num {
		case 1:
			concurrency.PrintNumbers()
		case 2:
			concurrency.WaitGroupDemo()
		case 3:
			concurrency.MutexDemo()
		case 4:
			concurrency.ChannelBasic()
		case 5:
			concurrency.BufferedChannel()
		case 6:
			concurrency.ChannelDirection()
		case 7:
			concurrency.SelectDemo()
		case 8:
			concurrency.SelectWithTimeout()
		case 9:
			concurrency.SelectNonBlocking()
		case 10:
			concurrency.ContextDemo()
		case 11:
			concurrency.ContextWithValue()
		case 12:
			concurrency.WorkerPoolDemo()
		case 13:
			concurrency.PipelineDemo()
		case 14:
			concurrency.FanOutFanInDemo()
		default:
			fmt.Println("无效选项，请选择 1-14")
		}

		fmt.Println()
	}

	os.Exit(0)
}
