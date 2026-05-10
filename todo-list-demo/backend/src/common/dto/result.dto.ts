export class Result<T = unknown> {
  code: number;
  message: string;
  data: T;

  constructor(code: number, message: string, data: T) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  static success<T>(data: T, message = 'success'): Result<T> {
    return new Result<T>(200, message, data);
  }

  static error(code: number, message: string): Result<null> {
    return new Result<null>(code, message, null);
  }
}

export class PageResult<T> {
  list: T[];
  total: number;
  page: number;
  pageSize: number;

  constructor(list: T[], total: number, page: number, pageSize: number) {
    this.list = list;
    this.total = total;
    this.page = page;
    this.pageSize = pageSize;
  }
}
