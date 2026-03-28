// Package models 定义数据模型（结构体）
// ============================================================
// Go 语言核心概念：结构体（Struct）
// ------------------------------------------------------------
// 结构体是 Go 中组织数据的主要方式，类似于其他语言的 class
// 但 Go 没有类和继承的概念，而是使用组合（composition）
// ============================================================
package models

import (
	"encoding/json"
	"time"
)

// ============================================================
// Todo 表示一个待办事项
// ------------------------------------------------------------
// 结构体标签（Struct Tags）:
// - `json:"name"` : 控制 JSON 序列化时的字段名
// - `json:"name,omitempty"` : 如果字段为零值，则不输出到 JSON
//
// 可见性规则（首字母大小写决定）:
// - 首字母大写: 可导出（public），可被其他包访问
// - 首字母小写: 不可导出（private），仅本包可访问
// ============================================================
type Todo struct {
	// ID 是任务的唯一标识符
	// 使用 uint 类型（无符号整数），只存储非负数
	ID uint `json:"id"`

	// Title 是任务标题
	// string 是 Go 的字符串类型，不可变
	Title string `json:"title"`

	// Description 是任务描述
	// omitempty 表示如果字段为空，JSON 序列化时省略该字段
	Description string `json:"description,omitempty"`

	// Completed 表示任务是否完成
	// bool 类型只有 true 和 false 两个值
	Completed bool `json:"completed"`

	// Priority 是任务优先级
	// 使用自定义类型 Priority，提高代码可读性
	Priority Priority `json:"priority"`

	// CreatedAt 是任务创建时间
	// time.Time 是 Go 标准库中的时间类型
	CreatedAt time.Time `json:"created_at"`

	// UpdatedAt 是任务最后更新时间
	UpdatedAt time.Time `json:"updated_at"`

	// Tags 是任务的标签列表
	// []string 是字符串切片（动态数组）
	Tags []string `json:"tags,omitempty"`
}

// ============================================================
// Priority 定义任务优先级类型
// ------------------------------------------------------------
// type 关键字可以创建自定义类型
// 这里基于 string 类型创建了一个新类型 Priority
// 这种模式常用于类型安全和代码可读性
// ============================================================
type Priority string

// 定义优先级常量
// iota 是 Go 的常量计数器，从 0 开始
// 但这里我们直接使用字符串值，更直观
const (
	PriorityLow    Priority = "low"
	PriorityMedium Priority = "medium"
	PriorityHigh   Priority = "high"
)

// ============================================================
// 方法（Method）
// ------------------------------------------------------------
// Go 的方法是带有接收者（receiver）的函数
// 接收者写在 func 关键字和方法名之间
// (t *Todo) 是指针接收者，可以修改结构体的字段
// (t Todo) 是值接收者，操作的是结构体的副本
//
// 何时使用指针接收者:
// 1. 需要修改接收者的字段
// 2. 结构体很大，避免复制开销
// 3. 保持一致性（如果一个方法用指针，其他也用指针）
// ============================================================

// Complete 将任务标记为完成
// 使用指针接收者，因为需要修改 Completed 字段
func (t *Todo) Complete() {
	t.Completed = true
	t.UpdatedAt = time.Now()
}

// Uncomplete 将任务标记为未完成
func (t *Todo) Uncomplete() {
	t.Completed = false
	t.UpdatedAt = time.Now()
}

// Toggle 切换任务的完成状态
func (t *Todo) Toggle() {
	t.Completed = !t.Completed
	t.UpdatedAt = time.Now()
}

// IsOverdue 检查任务是否过期（假设高优先级任务需要在24小时内完成）
// 使用值接收者，因为不需要修改字段
func (t Todo) IsOverdue() bool {
	if t.Priority != PriorityHigh || t.Completed {
		return false
	}
	return time.Since(t.CreatedAt) > 24*time.Hour
}

// ============================================================
// 接口（Interface）
// ------------------------------------------------------------
// 接口定义了一组方法签名
// 任何实现了这些方法的类型都隐式地实现了该接口
// 这是 Go 的"鸭子类型"（Duck Typing）
// ============================================================

// JSONAble 接口定义了可以序列化为 JSON 的行为
type JSONAble interface {
	ToJSON() ([]byte, error)
}

// ToJSON 实现 JSONAble 接口
// json.Marshal 将 Go 值转换为 JSON 字节切片
func (t Todo) ToJSON() ([]byte, error) {
	return json.Marshal(t)
}

// FromJSON 从 JSON 数据创建 Todo
// json.Unmarshal 将 JSON 字节切片转换为 Go 值
// 注意: 必须传递指针，否则无法修改结构体
func (t *Todo) FromJSON(data []byte) error {
	return json.Unmarshal(data, t)
}

// ============================================================
// 构造函数（Constructor）
// ------------------------------------------------------------
// Go 没有构造函数语法，通常使用 NewXxx 函数
// 这是一种约定，不是语言强制要求
// ============================================================

// NewTodo 创建一个新的 Todo 实例
// 返回指针，避免大结构体的复制开销
func NewTodo(title string, description string, priority Priority) *Todo {
	now := time.Now()
	return &Todo{
		Title:       title,
		Description: description,
		Completed:   false,
		Priority:    priority,
		CreatedAt:   now,
		UpdatedAt:   now,
		Tags:        []string{}, // 空切片，不是 nil
	}
}

// ============================================================
// String 方法
// ------------------------------------------------------------
// 实现 String() 方法，让 fmt 包能够友好地打印结构体
// 类似于 Java 的 toString() 或 Python 的 __str__
// ============================================================
func (t Todo) String() string {
	status := " "
	if t.Completed {
		status = "x"
	}
	return string(rune(t.ID)) + status + t.Title + t.Description
}
