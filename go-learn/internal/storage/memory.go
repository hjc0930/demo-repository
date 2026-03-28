// Package storage 提供数据存储实现
// ============================================================
// 存储层模式（Repository Pattern）
// ------------------------------------------------------------
// 将数据存储逻辑与业务逻辑分离
// 可以轻松切换不同的存储后端（内存、数据库、文件等）
// 只要实现相同的接口即可
// ============================================================
package storage

import (
	"encoding/json"
	"errors"
	"os"
	"sync"

	"github.com/hjc0930/go-learn/internal/models"
)

// ============================================================
// 自定义错误
// ------------------------------------------------------------
// 使用 errors.New() 创建简单的错误
// 使用 fmt.Errorf() 创建格式化的错误
// 也可以自定义错误类型，实现 Error() 方法
// ============================================================

var (
	// ErrNotFound 表示找不到指定的任务
	ErrNotFound = errors.New("task not found")

	// ErrAlreadyExists 表示任务已存在
	ErrAlreadyExists = errors.New("task already exists")

	// ErrInvalidID 表示无效的 ID
	ErrInvalidID = errors.New("invalid task id")
)

// ============================================================
// Repository 接口定义存储操作
// ------------------------------------------------------------
// 接口定义了一组方法签名，不包含实现
// 任何实现了这些方法的类型都满足此接口
//
// 接口设计原则:
// 1. 小而精（接口隔离原则）
// 2. 在使用方定义，而非实现方
// 3. 方法名清晰表达意图
// ============================================================
type Repository interface {
	// Create 创建新任务
	Create(todo *models.Todo) error

	// GetByID 根据 ID 获取任务
	GetByID(id uint) (*models.Todo, error)

	// GetAll 获取所有任务
	GetAll() ([]*models.Todo, error)

	// Update 更新任务
	Update(todo *models.Todo) error

	// Delete 删除任务
	Delete(id uint) error

	// FindByPriority 根据优先级查找任务
	FindByPriority(priority models.Priority) ([]*models.Todo, error)

	// FindCompleted 查找已完成的任务
	FindCompleted() ([]*models.Todo, error)
}

// ============================================================
// MemoryRepository 内存存储实现
// ------------------------------------------------------------
// 使用 map 存储数据，适合学习和测试
// 注意: 程序重启后数据会丢失
//
// sync.RWMutex 读写锁:
// - RLock()/RUnlock(): 读锁，允许多个读取者同时访问
// - Lock()/Unlock(): 写锁，独占访问
// 比互斥锁（sync.Mutex）更高效，因为读操作通常比写操作多
// ============================================================
type MemoryRepository struct {
	// mu 是读写锁，保护下面的 data map
	// 小写开头，不可导出，外部无法直接访问
	mu sync.RWMutex

	// data 存储所有任务，key 是任务 ID
	data map[uint]*models.Todo

	// nextID 用于生成唯一 ID
	nextID uint
}

// ============================================================
// NewMemoryRepository 创建内存存储实例
// ------------------------------------------------------------
// 这是工厂函数模式，用于创建和初始化结构体
// 返回接口类型，而不是具体类型，便于替换实现
// ============================================================
func NewMemoryRepository() Repository {
	return &MemoryRepository{
		data:   make(map[uint]*models.Todo),
		nextID: 1,
	}
}

// Create 创建新任务
// 使用指针接收者，因为需要修改内部状态
func (r *MemoryRepository) Create(todo *models.Todo) error {
	// 使用写锁，因为会修改 data map
	r.mu.Lock()
	defer r.mu.Unlock() // defer 确保函数退出时解锁

	// 分配 ID
	todo.ID = r.nextID
	r.nextID++

	// 存储任务
	r.data[todo.ID] = todo

	return nil
}

// GetByID 根据 ID 获取任务
func (r *MemoryRepository) GetByID(id uint) (*models.Todo, error) {
	// 使用读锁，因为只读取数据
	r.mu.RLock()
	defer r.mu.RUnlock()

	todo, exists := r.data[id]
	if !exists {
		// 使用 fmt.Errorf 包装错误，添加上下文信息
		return nil, ErrNotFound
	}

	// 返回副本，避免外部修改影响内部数据
	// 这是防御性编程的一种方式
	return r.copyTodo(todo), nil
}

// GetAll 获取所有任务
func (r *MemoryRepository) GetAll() ([]*models.Todo, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	// 创建切片，预分配容量提高性能
	// len(r.data) 是预期的大小，避免多次扩容
	result := make([]*models.Todo, 0, len(r.data))

	for _, todo := range r.data {
		result = append(result, r.copyTodo(todo))
	}

	return result, nil
}

// Update 更新任务
func (r *MemoryRepository) Update(todo *models.Todo) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.data[todo.ID]; !exists {
		return ErrNotFound
	}

	// 更新时间戳
	todo.UpdatedAt = todo.UpdatedAt

	r.data[todo.ID] = r.copyTodo(todo)
	return nil
}

// Delete 删除任务
func (r *MemoryRepository) Delete(id uint) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.data[id]; !exists {
		return ErrNotFound
	}

	// 从 map 中删除
	delete(r.data, id)
	return nil
}

// FindByPriority 根据优先级查找任务
func (r *MemoryRepository) FindByPriority(priority models.Priority) ([]*models.Todo, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	result := make([]*models.Todo, 0)

	for _, todo := range r.data {
		if todo.Priority == priority {
			result = append(result, r.copyTodo(todo))
		}
	}

	return result, nil
}

// FindCompleted 查找已完成的任务
func (r *MemoryRepository) FindCompleted() ([]*models.Todo, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	result := make([]*models.Todo, 0)

	for _, todo := range r.data {
		if todo.Completed {
			result = append(result, r.copyTodo(todo))
		}
	}

	return result, nil
}

// copyTodo 创建任务的深拷贝
// 私有方法，仅内部使用
func (r *MemoryRepository) copyTodo(todo *models.Todo) *models.Todo {
	// 复制 Tags 切片，避免共享底层数组
	tags := make([]string, len(todo.Tags))
	copy(tags, todo.Tags)

	return &models.Todo{
		ID:          todo.ID,
		Title:       todo.Title,
		Description: todo.Description,
		Completed:   todo.Completed,
		Priority:    todo.Priority,
		CreatedAt:   todo.CreatedAt,
		UpdatedAt:   todo.UpdatedAt,
		Tags:        tags,
	}
}

// ============================================================
// FileRepository 文件存储实现
// ------------------------------------------------------------
// 将数据持久化到 JSON 文件
// 使用互斥锁保护文件操作
// ============================================================
type FileRepository struct {
	mu       sync.Mutex
	filePath string
	data     map[uint]*models.Todo
	nextID   uint
}

// NewFileRepository 创建文件存储实例
func NewFileRepository(filePath string) (Repository, error) {
	repo := &FileRepository{
		filePath: filePath,
		data:     make(map[uint]*models.Todo),
		nextID:   1,
	}

	// 尝试从文件加载数据
	if err := repo.load(); err != nil && !os.IsNotExist(err) {
		return nil, err
	}

	return repo, nil
}

// load 从文件加载数据
func (r *FileRepository) load() error {
	data, err := os.ReadFile(r.filePath)
	if err != nil {
		return err
	}

	// 反序列化 JSON 到切片
	var todos []*models.Todo
	if err := json.Unmarshal(data, &todos); err != nil {
		return err
	}

	// 转换为 map
	for _, todo := range todos {
		if todo.ID >= r.nextID {
			r.nextID = todo.ID + 1
		}
		r.data[todo.ID] = todo
	}

	return nil
}

// save 保存数据到文件
func (r *FileRepository) save() error {
	// 转换 map 到切片
	todos := make([]*models.Todo, 0, len(r.data))
	for _, todo := range r.data {
		todos = append(todos, todo)
	}

	// 序列化为 JSON（带缩进，便于阅读）
	data, err := json.MarshalIndent(todos, "", "  ")
	if err != nil {
		return err
	}

	return os.WriteFile(r.filePath, data, 0644)
}

// Create 创建新任务
func (r *FileRepository) Create(todo *models.Todo) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	todo.ID = r.nextID
	r.nextID++

	r.data[todo.ID] = todo

	return r.save()
}

// GetByID 根据 ID 获取任务
func (r *FileRepository) GetByID(id uint) (*models.Todo, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	todo, exists := r.data[id]
	if !exists {
		return nil, ErrNotFound
	}

	return todo, nil
}

// GetAll 获取所有任务
func (r *FileRepository) GetAll() ([]*models.Todo, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := make([]*models.Todo, 0, len(r.data))
	for _, todo := range r.data {
		result = append(result, todo)
	}

	return result, nil
}

// Update 更新任务
func (r *FileRepository) Update(todo *models.Todo) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.data[todo.ID]; !exists {
		return ErrNotFound
	}

	r.data[todo.ID] = todo

	return r.save()
}

// Delete 删除任务
func (r *FileRepository) Delete(id uint) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.data[id]; !exists {
		return ErrNotFound
	}

	delete(r.data, id)

	return r.save()
}

// FindByPriority 根据优先级查找任务
func (r *FileRepository) FindByPriority(priority models.Priority) ([]*models.Todo, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := make([]*models.Todo, 0)
	for _, todo := range r.data {
		if todo.Priority == priority {
			result = append(result, todo)
		}
	}

	return result, nil
}

// FindCompleted 查找已完成的任务
func (r *FileRepository) FindCompleted() ([]*models.Todo, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := make([]*models.Todo, 0)
	for _, todo := range r.data {
		if todo.Completed {
			result = append(result, todo)
		}
	}

	return result, nil
}
