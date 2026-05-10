import { DomainError } from "@/shared/DomainError";

/**
 * 值对象：待办状态
 * 不可变，封装业务含义和展示逻辑
 */
export class TodoStatus {
  readonly value: number;
  readonly label: string;
  readonly color: string;

  private constructor(value: number, label: string, color: string) {
    this.value = value;
    this.label = label;
    this.color = color;
  }

  static readonly TODO = new TodoStatus(0, "待办", "#8c8c8c");
  static readonly IN_PROGRESS = new TodoStatus(1, "进行中", "#1890ff");
  static readonly DONE = new TodoStatus(2, "已完成", "#52c41a");

  private static readonly _values: TodoStatus[] = [
    TodoStatus.TODO,
    TodoStatus.IN_PROGRESS,
    TodoStatus.DONE,
  ];

  /** 从后端数值重建值对象 */
  static fromValue(v: number): TodoStatus {
    const found = this._values.find((s) => s.value === v);
    if (!found) {
      throw new DomainError(`无效的状态值: ${v}`);
    }
    return found;
  }

  /** 可选项列表（用于下拉等） */
  static all(): TodoStatus[] {
    return [...this._values];
  }

  /** 是否允许完成操作 */
  canComplete(): boolean {
    return this !== TodoStatus.DONE;
  }

  /** 是否允许标记为进行中 */
  canStartProgress(): boolean {
    return this === TodoStatus.TODO;
  }

  /** 是否已完成 */
  isDone(): boolean {
    return this === TodoStatus.DONE;
  }
}
