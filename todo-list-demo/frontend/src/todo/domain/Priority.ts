import { DomainError } from "@/shared/DomainError";

/**
 * 值对象：优先级
 * 不可变，封装比较逻辑和展示信息
 */
export class Priority {
  readonly value: number;
  readonly label: string;

  private constructor(value: number, label: string) {
    this.value = value;
    this.label = label;
  }

  static readonly LOW = new Priority(0, "低");
  static readonly MEDIUM = new Priority(1, "中");
  static readonly HIGH = new Priority(2, "高");

  private static readonly _values: Priority[] = [
    Priority.LOW,
    Priority.MEDIUM,
    Priority.HIGH,
  ];

  static fromValue(v: number): Priority {
    const found = this._values.find((p) => p.value === v);
    if (!found) {
      throw new DomainError(`无效的优先级值: ${v}`);
    }
    return found;
  }

  static all(): Priority[] {
    return [...this._values];
  }

  /** 当前优先级是否高于指定优先级 */
  isHigherThan(other: Priority): boolean {
    return this.value > other.value;
  }
}
