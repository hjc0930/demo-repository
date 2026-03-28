// Package service_test - 服务层测试
package service_test

import (
	"testing"

	"github.com/hjc0930/go-learn/internal/models"
	"github.com/hjc0930/go-learn/internal/service"
	"github.com/hjc0930/go-learn/internal/storage"
)

// 测试辅助函数：创建测试用的服务
func setupTestService() *service.TodoService {
	repo := storage.NewMemoryRepository()
	return service.NewTodoService(repo)
}

// TestTodoService_CreateTodo 测试创建任务
func TestTodoService_CreateTodo(t *testing.T) {
	svc := setupTestService()

	tests := []struct {
		name    string
		req     service.CreateTodoRequest
		wantErr bool
	}{
		{
			name: "正常创建",
			req: service.CreateTodoRequest{
				Title:       "学习 Go",
				Description: "完成基础教程",
				Priority:    models.PriorityHigh,
			},
			wantErr: false,
		},
		{
			name: "空标题",
			req: service.CreateTodoRequest{
				Title:       "",
				Description: "",
				Priority:    models.PriorityLow,
			},
			wantErr: true,
		},
		{
			name: "无效优先级",
			req: service.CreateTodoRequest{
				Title:       "Test",
				Description: "",
				Priority:    "invalid",
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			todo, err := svc.CreateTodo(tt.req)

			if tt.wantErr {
				if err == nil {
					t.Error("expected error, got nil")
				}
				return
			}

			if err != nil {
				t.Errorf("unexpected error: %v", err)
				return
			}

			if todo.Title != tt.req.Title {
				t.Errorf("Title = %v, want %v", todo.Title, tt.req.Title)
			}
			if todo.ID == 0 {
				t.Error("ID should be assigned")
			}
		})
	}
}

// TestTodoService_GetTodo 测试获取任务
func TestTodoService_GetTodo(t *testing.T) {
	svc := setupTestService()

	// 先创建一个任务
	created, _ := svc.CreateTodo(service.CreateTodoRequest{
		Title:    "Test",
		Priority: models.PriorityMedium,
	})

	// 测试获取
	todo, err := svc.GetTodo(created.ID)
	if err != nil {
		t.Fatalf("GetTodo() error = %v", err)
	}

	if todo.ID != created.ID {
		t.Errorf("ID = %v, want %v", todo.ID, created.ID)
	}

	// 测试获取不存在的任务
	_, err = svc.GetTodo(999)
	if err == nil {
		t.Error("expected error for non-existent todo")
	}
}

// TestTodoService_ListTodos 测试列出任务
func TestTodoService_ListTodos(t *testing.T) {
	svc := setupTestService()

	// 创建多个任务
	svc.CreateTodo(service.CreateTodoRequest{Title: "Task 1", Priority: models.PriorityHigh})
	svc.CreateTodo(service.CreateTodoRequest{Title: "Task 2", Priority: models.PriorityLow})
	svc.CreateTodo(service.CreateTodoRequest{Title: "Task 3", Priority: models.PriorityHigh})

	// 获取所有任务
	all, err := svc.ListTodos(nil, nil)
	if err != nil {
		t.Fatalf("ListTodos() error = %v", err)
	}

	if len(all) != 3 {
		t.Errorf("len(all) = %v, want 3", len(all))
	}

	// 按优先级过滤
	highPriority := models.PriorityHigh
	high, err := svc.ListTodos(&highPriority, nil)
	if err != nil {
		t.Fatalf("ListTodos(priority) error = %v", err)
	}

	if len(high) != 2 {
		t.Errorf("len(high) = %v, want 2", len(high))
	}
}

// TestTodoService_UpdateTodo 测试更新任务
func TestTodoService_UpdateTodo(t *testing.T) {
	svc := setupTestService()

	// 创建任务
	created, _ := svc.CreateTodo(service.CreateTodoRequest{
		Title:    "Original",
		Priority: models.PriorityLow,
	})

	// 更新任务
	newTitle := "Updated"
	newPriority := models.PriorityHigh
	updated, err := svc.UpdateTodo(created.ID, service.UpdateTodoRequest{
		Title:    &newTitle,
		Priority: &newPriority,
	})

	if err != nil {
		t.Fatalf("UpdateTodo() error = %v", err)
	}

	if updated.Title != newTitle {
		t.Errorf("Title = %v, want %v", updated.Title, newTitle)
	}
	if updated.Priority != newPriority {
		t.Errorf("Priority = %v, want %v", updated.Priority, newPriority)
	}
}

// TestTodoService_DeleteTodo 测试删除任务
func TestTodoService_DeleteTodo(t *testing.T) {
	svc := setupTestService()

	// 创建任务
	created, _ := svc.CreateTodo(service.CreateTodoRequest{
		Title:    "To Delete",
		Priority: models.PriorityLow,
	})

	// 删除
	err := svc.DeleteTodo(created.ID)
	if err != nil {
		t.Fatalf("DeleteTodo() error = %v", err)
	}

	// 验证已删除
	_, err = svc.GetTodo(created.ID)
	if err == nil {
		t.Error("todo should be deleted")
	}
}

// TestTodoService_GetStats 测试统计
func TestTodoService_GetStats(t *testing.T) {
	svc := setupTestService()

	// 创建多个任务
	svc.CreateTodo(service.CreateTodoRequest{Title: "Task 1", Priority: models.PriorityHigh})
	svc.CreateTodo(service.CreateTodoRequest{Title: "Task 2", Priority: models.PriorityLow})
	task3, _ := svc.CreateTodo(service.CreateTodoRequest{Title: "Task 3", Priority: models.PriorityMedium})

	// 完成一个任务
	svc.CompleteTodo(task3.ID)

	// 获取统计
	stats, err := svc.GetStats()
	if err != nil {
		t.Fatalf("GetStats() error = %v", err)
	}

	if stats.Total != 3 {
		t.Errorf("Total = %v, want 3", stats.Total)
	}
	if stats.Completed != 1 {
		t.Errorf("Completed = %v, want 1", stats.Completed)
	}
	if stats.Pending != 2 {
		t.Errorf("Pending = %v, want 2", stats.Pending)
	}
}
