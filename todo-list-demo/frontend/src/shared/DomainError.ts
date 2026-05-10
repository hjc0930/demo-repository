/**
 * 领域异常
 * 用于表示领域规则被违反，与应用层技术异常区分
 */
export class DomainError extends Error {
  constructor(message: string) {
    super(message);
    this.name = "DomainError";
  }
}
