import { Todo } from "./Todo";
import { TodoStatus } from "./TodoStatus";
import type { TodoApiData, PaginatedData } from "@/shared/ApiResponse";

/**
 * 聚合根：待办列表
 * 包含 Todo 集合和分页信息，封装集合级别的业务规则
 */
export class TodoList {
  readonly total: number;
  readonly page: number;
  readonly pageSize: number;
  private _items: Todo[];

  private constructor(
    items: Todo[],
    total: number,
    page: number,
    pageSize: number,
  ) {
    this._items = items;
    this.total = total;
    this.page = page;
    this.pageSize = pageSize;
  }

  get items(): ReadonlyArray<Todo> {
    return this._items;
  }

  get isEmpty(): boolean {
    return this._items.length === 0;
  }

  // ===== 查询 =====

  /** 按状态筛选后返回新的 TodoList */
  filterByStatus(status: TodoStatus): TodoList {
    return new TodoList(
      this._items.filter((t) => t.status.value === status.value),
      this.total,
      this.page,
      this.pageSize,
    );
  }

  /** 已完成的待办数量 */
  get completedCount(): number {
    return this._items.filter((t) => t.isDone()).length;
  }

  /** 逾期的待办 */
  get overdueItems(): Todo[] {
    return this._items.filter((t) => t.isOverdue());
  }

  // ===== 命令 =====

  /** 在列表中添加一项（用于乐观更新） */
  addItem(todo: Todo): void {
    this._items = [todo, ...this._items];
  }

  /** 从列表中移除一项（用于乐观删除） */
  removeItem(id: number): void {
    this._items = this._items.filter((t) => t.id !== id);
  }

  /** 更新列表中匹配 ID 的待办（用于乐观更新） */
  updateItem(updated: Todo): void {
    this._items = this._items.map((t) => (t.id === updated.id ? updated : t));
  }

  // ===== 工厂 =====

  /** 从 API 分页数据重建领域对象 */
  static fromApi(raw: PaginatedData<TodoApiData>): TodoList {
    return new TodoList(
      raw.list.map(Todo.fromApi),
      raw.total,
      raw.page,
      raw.pageSize,
    );
  }
}
