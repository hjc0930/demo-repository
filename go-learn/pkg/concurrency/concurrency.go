// Package concurrency 演示 Go 并发编程
// ============================================================
// Go 并发编程核心概念
// ------------------------------------------------------------
// Go 的并发模型基于 CSP (Communicating Sequential Processes)
// 核心理念: "不要通过共享内存来通信，而是通过通信来共享内存"
//
// 主要组件:
// 1. Goroutine: 轻量级线程
// 2. Channel: goroutine 之间的通信管道
// 3. Select: 多路复用，处理多个 channel
// 4. Sync 包: 互斥锁、等待组等同步原语
// ============================================================
package concurrency

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/hjc0930/go-learn/internal/models"
)

// ============================================================
// Goroutine 基础示例
// ============================================================

// PrintNumbers 演示基本的 goroutine 使用
// goroutine 是 Go 运行时管理的轻量级线程
// 启动一个 goroutine 只需在函数调用前加 go 关键字
func PrintNumbers() {
	fmt.Println("=== Goroutine 基础示例 ===")

	// 启动一个 goroutine
	// go 关键字会在新的 goroutine 中执行这个函数
	go func() {
		for i := 1; i <= 5; i++ {
			fmt.Printf("goroutine: %d\n", i)
			time.Sleep(100 * time.Millisecond)
		}
	}()

	// 主 goroutine 继续执行
	for i := 1; i <= 5; i++ {
		fmt.Printf("main: %d\n", i)
		time.Sleep(100 * time.Millisecond)
	}

	// 注意: 如果主 goroutine 结束，所有其他 goroutine 也会终止
}

// ============================================================
// WaitGroup 等待一组 goroutine 完成
// ============================================================

// WaitGroupDemo 演示 sync.WaitGroup 的使用
// WaitGroup 用于等待一组 goroutine 完成
// 主要方法:
// - Add(delta int): 增加计数器
// - Done(): 减少计数器（等于 Add(-1)）
// - Wait(): 阻塞直到计数器为零
func WaitGroupDemo() {
	fmt.Println("\n=== WaitGroup 示例 ===")

	var wg sync.WaitGroup

	// 启动 3 个 goroutine
	for i := 1; i <= 3; i++ {
		// 在启动 goroutine 前增加计数器
		wg.Add(1)

		go func(id int) {
			// 确保在函数退出时调用 Done
			defer wg.Done()

			fmt.Printf("Worker %d 开始工作\n", id)
			time.Sleep(time.Duration(id) * 100 * time.Millisecond)
			fmt.Printf("Worker %d 完成工作\n", id)
		}(i) // 将 i 作为参数传入，避免闭包问题
	}

	// 等待所有 goroutine 完成
	wg.Wait()
	fmt.Println("所有 Worker 完成")
}

// ============================================================
// Mutex 互斥锁
// ============================================================

// Counter 演示需要互斥锁保护的共享资源
type Counter struct {
	mu    sync.Mutex
	value int
}

// Increment 安全地增加计数器
func (c *Counter) Increment() {
	c.mu.Lock()   // 获取锁
	defer c.mu.Unlock() // 函数退出时释放锁
	c.value++
}

// Value 获取当前值
func (c *Counter) Value() int {
	c.mu.Lock()
	defer c.mu.Unlock()
	return c.value
}

// MutexDemo 演示互斥锁的使用
func MutexDemo() {
	fmt.Println("\n=== Mutex 示例 ===")

	counter := &Counter{}
	var wg sync.WaitGroup

	// 启动 1000 个 goroutine 同时增加计数器
	for i := 0; i < 1000; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			counter.Increment()
		}()
	}

	wg.Wait()
	fmt.Printf("最终计数: %d (期望: 1000)\n", counter.Value())
}

// ============================================================
// Channel 通道
// ============================================================

// ChannelBasic 演示基本的 channel 操作
// channel 是 goroutine 之间通信的管道
// 分为无缓冲 channel 和有缓冲 channel
func ChannelBasic() {
	fmt.Println("\n=== Channel 基础示例 ===")

	// 创建无缓冲 channel
	// 无缓冲 channel 发送和接收是同步的
	ch := make(chan string)

	// 启动一个 goroutine 发送数据
	go func() {
		ch <- "Hello from goroutine!" // 发送数据
	}()

	// 在主 goroutine 中接收数据
	msg := <-ch // 接收数据，会阻塞直到有数据
	fmt.Println("收到消息:", msg)
}

// BufferedChannel 演示有缓冲 channel
// 有缓冲 channel 可以存储多个值
// 只有缓冲区满时发送才会阻塞，缓冲区空时接收才会阻塞
func BufferedChannel() {
	fmt.Println("\n=== 有缓冲 Channel 示例 ===")

	// 创建容量为 2 的有缓冲 channel
	ch := make(chan int, 2)

	// 可以连续发送两个值而不阻塞
	ch <- 1
	ch <- 2

	fmt.Println("发送了两个值")

	// 接收值
	fmt.Println("接收:", <-ch)
	fmt.Println("接收:", <-ch)
}

// ChannelDirection 演示定向 channel
// 可以指定 channel 只发送或只接收，提高类型安全
func ChannelDirection() {
	fmt.Println("\n=== 定向 Channel 示例 ===")

	// sendOnly 只能发送
	sendOnly := func(ch chan<- int) {
		ch <- 42
		// ch = <-ch // 编译错误！不能从只发送 channel 接收
	}

	// receiveOnly 只能接收
	receiveOnly := func(ch <-chan int) int {
		return <-ch
		// ch <- 1 // 编译错误！不能向只接收 channel 发送
	}

	ch := make(chan int, 1)
	sendOnly(ch)
	value := receiveOnly(ch)
	fmt.Println("收到值:", value)
}

// ============================================================
// Select 多路复用
// ============================================================

// SelectDemo 演示 select 语句
// select 让你可以同时等待多个 channel 操作
// 类似于 switch，但用于 channel
func SelectDemo() {
	fmt.Println("\n=== Select 示例 ===")

	ch1 := make(chan string)
	ch2 := make(chan string)

	// 启动两个 goroutine 发送数据
	go func() {
		time.Sleep(100 * time.Millisecond)
		ch1 <- "来自 ch1"
	}()

	go func() {
		time.Sleep(200 * time.Millisecond)
		ch2 <- "来自 ch2"
	}()

	// 使用 select 等待多个 channel
	for i := 0; i < 2; i++ {
		select {
		case msg1 := <-ch1:
			fmt.Println("收到:", msg1)
		case msg2 := <-ch2:
			fmt.Println("收到:", msg2)
		}
	}
}

// SelectWithTimeout 演示带超时的 select
func SelectWithTimeout() {
	fmt.Println("\n=== Select 超时示例 ===")

	ch := make(chan string)

	go func() {
		time.Sleep(2 * time.Second)
		ch <- "延迟的消息"
	}()

	select {
	case msg := <-ch:
		fmt.Println("收到:", msg)
	case <-time.After(500 * time.Millisecond):
		// time.After 返回一个 channel，在指定时间后发送当前时间
		fmt.Println("超时！等待太久了")
	}
}

// SelectNonBlocking 演示非阻塞 select
// 使用 default 分支实现非阻塞操作
func SelectNonBlocking() {
	fmt.Println("\n=== 非阻塞 Select 示例 ===")

	ch := make(chan int, 1)

	// 非阻塞发送
	select {
	case ch <- 1:
		fmt.Println("发送成功")
	default:
		fmt.Println("channel 满了，发送失败")
	}

	// 非阻塞接收
	select {
	case val := <-ch:
		fmt.Println("接收成功:", val)
	default:
		fmt.Println("channel 空的，没有数据")
	}
}

// ============================================================
// Context 上下文
// ============================================================

// ContextDemo 演示 context 的使用
// context 用于:
// 1. 取消操作
// 2. 超时控制
// 3. 传递请求范围的值
func ContextDemo() {
	fmt.Println("\n=== Context 示例 ===")

	// 创建一个带超时的 context
	// context.Background() 返回一个空的 context，通常作为根 context
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	// defer cancel() 很重要，即使操作完成也要调用，释放资源
	defer cancel()

	// 启动一个耗时操作
	go func() {
		// 模拟耗时操作
		time.Sleep(3 * time.Second)
		fmt.Println("操作完成（但这不会打印，因为会被取消）")
	}()

	// 等待操作完成或超时
	select {
	case <-ctx.Done():
		// ctx.Done() 返回一个 channel，当 context 被取消或超时时关闭
		fmt.Println("操作被取消:", ctx.Err())
	}
}

// ContextWithValue 演示 context 传递值
func ContextWithValue() {
	fmt.Println("\n=== Context 传值示例 ===")

	// 不要使用内置类型作为 key，避免冲突
	type contextKey string
	const userIDKey contextKey = "userID"

	// 创建带有值的 context
	ctx := context.WithValue(context.Background(), userIDKey, "user123")

	// 在函数链中传递和获取值
	processRequest := func(ctx context.Context) {
		if userID, ok := ctx.Value(userIDKey).(string); ok {
			fmt.Println("用户 ID:", userID)
		} else {
			fmt.Println("未找到用户 ID")
		}
	}

	processRequest(ctx)
}

// ============================================================
// Worker Pool 工作池模式
// ============================================================

// Job 定义任务
type Job struct {
	ID   int
	Todo *models.Todo
}

// Result 定义结果
type Result struct {
	JobID int
	Error error
}

// WorkerPool 工作池
type WorkerPool struct {
	workerCount int
	jobs        chan Job
	results     chan Result
	wg          sync.WaitGroup
}

// NewWorkerPool 创建工作池
func NewWorkerPool(workerCount int) *WorkerPool {
	return &WorkerPool{
		workerCount: workerCount,
		jobs:        make(chan Job, 100),
		results:     make(chan Result, 100),
	}
}

// Start 启动工作池
func (p *WorkerPool) Start() {
	// 启动指定数量的 worker
	for i := 0; i < p.workerCount; i++ {
		p.wg.Add(1)
		go p.worker(i)
	}
}

// worker 处理任务
func (p *WorkerPool) worker(id int) {
	defer p.wg.Done()

	// 持续从 jobs channel 获取任务
	// 当 jobs channel 关闭时，for range 会自动退出
	for job := range p.jobs {
		fmt.Printf("Worker %d 处理任务 %d\n", id, job.ID)

		// 模拟处理
		time.Sleep(100 * time.Millisecond)

		// 发送结果
		p.results <- Result{
			JobID: job.ID,
			Error: nil,
		}
	}
}

// Submit 提交任务
func (p *WorkerPool) Submit(job Job) {
	p.jobs <- job
}

// Stop 停止工作池
func (p *WorkerPool) Stop() {
	close(p.jobs) // 关闭 jobs channel，通知所有 worker 退出
	p.wg.Wait()   // 等待所有 worker 完成
	close(p.results)
}

// Results 返回结果 channel
func (p *WorkerPool) Results() <-chan Result {
	return p.results
}

// WorkerPoolDemo 演示工作池
func WorkerPoolDemo() {
	fmt.Println("\n=== Worker Pool 示例 ===")

	pool := NewWorkerPool(3) // 3 个 worker
	pool.Start()

	// 启动一个 goroutine 收集结果
	go func() {
		for result := range pool.Results() {
			fmt.Printf("任务 %d 完成\n", result.JobID)
		}
	}()

	// 提交 10 个任务
	for i := 0; i < 10; i++ {
		pool.Submit(Job{ID: i})
	}

	pool.Stop()
	fmt.Println("工作池已停止")
}

// ============================================================
// Pipeline 管道模式
// ============================================================

// PipelineDemo 演示管道模式
// 管道模式将复杂处理分解为多个阶段
// 每个阶段通过 channel 连接
func PipelineDemo() {
	fmt.Println("\n=== Pipeline 示例 ===")

	// 阶段1: 生成数据
	generate := func(nums ...int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for _, n := range nums {
				out <- n
			}
		}()
		return out
	}

	// 阶段2: 处理数据（平方）
	square := func(in <-chan int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for n := range in {
				out <- n * n
			}
		}()
		return out
	}

	// 阶段3: 处理数据（加倍）
	double := func(in <-chan int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for n := range in {
				out <- n * 2
			}
		}()
		return out
	}

	// 构建管道
	nums := generate(1, 2, 3, 4, 5)
	squared := square(nums)
	doubled := double(squared)

	// 消费结果
	fmt.Print("管道结果: ")
	for n := range doubled {
		fmt.Print(n, " ")
	}
	fmt.Println()
}

// ============================================================
// Fan-Out Fan-In 扇出扇入模式
// ============================================================

// FanOutFanInDemo 演示扇出扇入模式
// Fan-Out: 多个 goroutine 从同一个 channel 读取
// Fan-In: 多个 channel 的结果合并到一个 channel
func FanOutFanInDemo() {
	fmt.Println("\n=== Fan-Out Fan-In 示例 ===")

	// 生成数据
	generate := func(count int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for i := 0; i < count; i++ {
				out <- i
			}
		}()
		return out
	}

	// 处理函数（模拟耗时操作）
	process := func(in <-chan int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for n := range in {
				time.Sleep(50 * time.Millisecond)
				out <- n * n
			}
		}()
		return out
	}

	// 合并多个 channel
	merge := func(channels ...<-chan int) <-chan int {
		var wg sync.WaitGroup
		out := make(chan int)

		// 为每个 channel 启动一个 goroutine
		output := func(c <-chan int) {
			defer wg.Done()
			for n := range c {
				out <- n
			}
		}

		wg.Add(len(channels))
		for _, c := range channels {
			go output(c)
		}

		// 等待所有 channel 处理完成
		go func() {
			wg.Wait()
			close(out)
		}()

		return out
	}

	// 生成数据
	input := generate(10)

	// Fan-Out: 启动多个 worker 处理同一个输入
	c1 := process(input)
	c2 := process(input)
	c3 := process(input)

	// Fan-In: 合并结果
	result := merge(c1, c2, c3)

	// 收集结果
	fmt.Print("Fan-Out Fan-In 结果: ")
	for n := range result {
		fmt.Print(n, " ")
	}
	fmt.Println()
}

// ============================================================
// 运行所有示例
// ============================================================

// RunAllDemos 运行所有并发示例
func RunAllDemos() {
	PrintNumbers()
	WaitGroupDemo()
	MutexDemo()
	ChannelBasic()
	BufferedChannel()
	ChannelDirection()
	SelectDemo()
	SelectWithTimeout()
	SelectNonBlocking()
	ContextDemo()
	ContextWithValue()
	WorkerPoolDemo()
	PipelineDemo()
	FanOutFanInDemo()
}
