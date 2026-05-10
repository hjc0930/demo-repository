import { TodoStatus } from "./TodoStatus";
import { Priority } from "./Priority";
import { DomainError } from "@/shared/DomainError";
import type { TodoApiData } from "@/shared/ApiResponse";

/**
 * 实体：待办事项
 * 有唯一标识（id），封装业务规则和行为
 */
export class Todo {
  readonly id: number;
  readonly createdAt: Date;
  readonly updatedAt: Date;
  private _status: TodoStatus;
  private _priority: Priority;
  private _title: string;
  private _description: string;
  private _dueDate: Date | null;

  private constructor(
    id: number,
    title: string,
    description: string,
    status: TodoStatus,
    priority: Priority,
    dueDate: Date | null,
    createdAt: Date,
    updatedAt: Date,
  ) {
    this.id = id;
    this._title = title;
    this._description = description;
    this._status = status;
    this._priority = priority;
    this._dueDate = dueDate;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // ===== 查询方法 =====

  get title(): string {
    return this._title;
  }

  get description(): string {
    return this._description;
  }

  get status(): TodoStatus {
    return this._status;
  }

  get priority(): Priority {
    return this._priority;
  }

  get dueDate(): Date | null {
    return this._dueDate;
  }

  isDone(): boolean {
    return this._status.isDone();
  }

  isOverdue(): boolean {
    if (!this._dueDate || this.isDone()) return false;
    return new Date() > this._dueDate;
  }

  // ===== 命令方法（改变状态，带业务规则校验） =====

  complete(): void {
    if (!this._status.canComplete()) {
      throw new DomainError("该待办已完成，无法重复完成");
    }
    this._status = TodoStatus.DONE;
  }

  startProgress(): void {
    if (!this._status.canStartProgress()) {
      throw new DomainError("该待办无法标记为进行中");
    }
    this._status = TodoStatus.IN_PROGRESS;
  }

  changePriority(priority: Priority): void {
    this._priority = priority;
  }

  updateTitle(title: string): void {
    if (!title || title.trim().length === 0) return;
    this._title = title;
  }

  updateDescription(description: string): void {
    this._description = description;
  }

  updateDueDate(dueDate: Date | null): void {
    this._dueDate = dueDate;
  }

  // ===== 工厂方法 =====

  /** 从创建表单数据生成 API payload */
  static create(input: {
    title: string;
    description?: string;
    priority?: number;
    dueDate?: string;
  }): { title: string; description: string; status: number; priority: number; dueDate?: string } {
    return {
      title: input.title,
      description: input.description ?? "",
      status: TodoStatus.TODO.value,
      priority: input.priority ?? Priority.MEDIUM.value,
      dueDate: input.dueDate || undefined,
    };
  }

  /** 从 API 原始数据重建领域对象 */
  static fromApi(raw: TodoApiData): Todo {
    return new Todo(
      raw.id,
      raw.title,
      raw.description,
      TodoStatus.fromValue(raw.status),
      Priority.fromValue(raw.priority),
      raw.dueDate ? new Date(raw.dueDate) : null,
      new Date(raw.createdAt),
      new Date(raw.updatedAt),
    );
  }

  /** 生成用于更新 API 的 payload */
  toUpdatePayload(): Partial<{
    title: string;
    description: string;
    status: number;
    priority: number;
    dueDate: string | null;
  }> {
    return {
      title: this._title,
      description: this._description,
      status: this._status.value,
      priority: this._priority.value,
      dueDate: this._dueDate?.toISOString() ?? null,
    };
  }
}
