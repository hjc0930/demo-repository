// Package models_test - 模型测试
// ============================================================
// Go 单元测试
// ------------------------------------------------------------
// 测试文件命名规则: xxx_test.go
// 测试函数命名规则: TestXxx，参数为 *testing.T
// 运行测试: go test ./...
// 运行特定测试: go test -run TestXxx
// 查看覆盖率: go test -cover
// ============================================================
package models_test

import (
	"encoding/json"
	"testing"
	"time"

	"github.com/hjc0930/go-learn/internal/models"
)

// ============================================================
// 表驱动测试（Table-Driven Tests）
// ------------------------------------------------------------
// Go 推荐的测试模式，使用结构体切片定义测试用例
// 优点:
// 1. 易于添加新测试用例
// 2. 测试代码简洁
// 3. 测试逻辑与数据分离
// ============================================================

// TestNewTodo 测试创建任务
func TestNewTodo(t *testing.T) {
	// 定义测试用例
	tests := []struct {
		name        string
		title       string
		description string
		priority    models.Priority
		wantErr     bool
	}{
		{
			name:        "正常创建",
			title:       "学习 Go",
			description: "完成基础教程",
			priority:    models.PriorityHigh,
			wantErr:     false,
		},
		{
			name:        "空标题",
			title:       "",
			description: "",
			priority:    models.PriorityLow,
			wantErr:     false, // NewTodo 不验证，验证在 service 层
		},
	}

	// 遍历测试用例
	for _, tt := range tests {
		// 使用 t.Run 创建子测试
		t.Run(tt.name, func(t *testing.T) {
			todo := models.NewTodo(tt.title, tt.description, tt.priority)

			// 验证结果
			if todo.Title != tt.title {
				t.Errorf("Title = %v, want %v", todo.Title, tt.title)
			}
			if todo.Description != tt.description {
				t.Errorf("Description = %v, want %v", todo.Description, tt.description)
			}
			if todo.Priority != tt.priority {
				t.Errorf("Priority = %v, want %v", todo.Priority, tt.priority)
			}
			if todo.Completed {
				t.Error("New todo should not be completed")
			}
			if todo.CreatedAt.IsZero() {
				t.Error("CreatedAt should be set")
			}
		})
	}
}

// TestTodo_Complete 测试完成方法
func TestTodo_Complete(t *testing.T) {
	todo := models.NewTodo("Test", "", models.PriorityMedium)

	// 初始状态应该是未完成
	if todo.Completed {
		t.Fatal("New todo should not be completed")
	}

	// 记录原始更新时间
	originalUpdatedAt := todo.UpdatedAt

	// 等待一小段时间确保时间差异
	time.Sleep(10 * time.Millisecond)

	// 标记完成
	todo.Complete()

	// 验证
	if !todo.Completed {
		t.Error("Todo should be completed")
	}
	if !todo.UpdatedAt.After(originalUpdatedAt) {
		t.Error("UpdatedAt should be updated")
	}
}

// TestTodo_Toggle 测试切换状态
func TestTodo_Toggle(t *testing.T) {
	todo := models.NewTodo("Test", "", models.PriorityMedium)

	// 初始状态
	if todo.Completed {
		t.Fatal("Initial state should be uncompleted")
	}

	// 第一次切换 -> 完成
	todo.Toggle()
	if !todo.Completed {
		t.Error("After first toggle, should be completed")
	}

	// 第二次切换 -> 未完成
	todo.Toggle()
	if todo.Completed {
		t.Error("After second toggle, should be uncompleted")
	}
}

// TestTodo_IsOverdue 测试过期检查
func TestTodo_IsOverdue(t *testing.T) {
	tests := []struct {
		name      string
		priority  models.Priority
		completed bool
		createdAt time.Time
		want      bool
	}{
		{
			name:      "高优先级未过期",
			priority:  models.PriorityHigh,
			completed: false,
			createdAt: time.Now(),
			want:      false,
		},
		{
			name:      "高优先级已过期",
			priority:  models.PriorityHigh,
			completed: false,
			createdAt: time.Now().Add(-25 * time.Hour), // 25 小时前
			want:      true,
		},
		{
			name:      "高优先级但已完成",
			priority:  models.PriorityHigh,
			completed: true,
			createdAt: time.Now().Add(-25 * time.Hour),
			want:      false,
		},
		{
			name:      "中优先级不算过期",
			priority:  models.PriorityMedium,
			completed: false,
			createdAt: time.Now().Add(-25 * time.Hour),
			want:      false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			todo := &models.Todo{
				Priority:  tt.priority,
				Completed: tt.completed,
				CreatedAt: tt.createdAt,
			}

			got := todo.IsOverdue()
			if got != tt.want {
				t.Errorf("IsOverdue() = %v, want %v", got, tt.want)
			}
		})
	}
}

// TestTodo_JSON 测试 JSON 序列化
func TestTodo_JSON(t *testing.T) {
	original := &models.Todo{
		ID:          1,
		Title:       "Test Todo",
		Description: "Test Description",
		Completed:   false,
		Priority:    models.PriorityHigh,
		CreatedAt:   time.Date(2024, 1, 1, 12, 0, 0, 0, time.UTC),
		UpdatedAt:   time.Date(2024, 1, 1, 12, 0, 0, 0, time.UTC),
		Tags:        []string{"test", "example"},
	}

	// 序列化
	data, err := json.Marshal(original)
	if err != nil {
		t.Fatalf("Failed to marshal: %v", err)
	}

	// 反序列化
	var parsed models.Todo
	if err := json.Unmarshal(data, &parsed); err != nil {
		t.Fatalf("Failed to unmarshal: %v", err)
	}

	// 验证
	if parsed.ID != original.ID {
		t.Errorf("ID = %v, want %v", parsed.ID, original.ID)
	}
	if parsed.Title != original.Title {
		t.Errorf("Title = %v, want %v", parsed.Title, original.Title)
	}
	if parsed.Priority != original.Priority {
		t.Errorf("Priority = %v, want %v", parsed.Priority, original.Priority)
	}
}

// TestTodo_ToJSON 测试 ToJSON 方法
func TestTodo_ToJSON(t *testing.T) {
	todo := models.NewTodo("Test", "Description", models.PriorityHigh)

	data, err := todo.ToJSON()
	if err != nil {
		t.Fatalf("ToJSON() error = %v", err)
	}

	// 验证是有效的 JSON
	var parsed models.Todo
	if err := json.Unmarshal(data, &parsed); err != nil {
		t.Fatalf("Invalid JSON: %v", err)
	}
}

// TestTodo_FromJSON 测试 FromJSON 方法
func TestTodo_FromJSON(t *testing.T) {
	jsonData := `{"id":1,"title":"Test","description":"Desc","completed":true,"priority":"high"}`

	todo := &models.Todo{}
	err := todo.FromJSON([]byte(jsonData))
	if err != nil {
		t.Fatalf("FromJSON() error = %v", err)
	}

	if todo.ID != 1 {
		t.Errorf("ID = %v, want 1", todo.ID)
	}
	if todo.Title != "Test" {
		t.Errorf("Title = %v, want Test", todo.Title)
	}
	if !todo.Completed {
		t.Error("Completed should be true")
	}
}

// ============================================================
// 基准测试（Benchmark）
// ------------------------------------------------------------
// 测试函数命名: BenchmarkXxx，参数为 *testing.B
// 运行: go test -bench=.
// b.N 会自动调整，直到结果稳定
// ============================================================

// BenchmarkNewTodo 基准测试创建任务
func BenchmarkNewTodo(b *testing.B) {
	for i := 0; i < b.N; i++ {
		_ = models.NewTodo("Test", "Description", models.PriorityMedium)
	}
}

// BenchmarkTodo_ToJSON 基准测试 JSON 序列化
func BenchmarkTodo_ToJSON(b *testing.B) {
	todo := models.NewTodo("Test", "Description", models.PriorityHigh)
	todo.Tags = []string{"tag1", "tag2", "tag3"}

	b.ResetTimer() // 重置计时器，排除准备时间

	for i := 0; i < b.N; i++ {
		_, _ = todo.ToJSON()
	}
}

// ============================================================
// 示例测试（Example）
// ------------------------------------------------------------
// 以 "Example" 开头的函数
// 输出通过 "Output:" 注释验证
// 运行: go test -v
// ============================================================

// ExampleNewTodo 演示创建任务
func ExampleNewTodo() {
	todo := models.NewTodo("学习 Go", "完成基础教程", models.PriorityHigh)
	fmt.Println(todo.Title)
	// Output: 学习 Go
}

// ExampleTodo_Complete 演示完成任务
func ExampleTodo_Complete() {
	todo := models.NewTodo("Test", "", models.PriorityMedium)
	todo.Complete()
	fmt.Println(todo.Completed)
	// Output: true
}
