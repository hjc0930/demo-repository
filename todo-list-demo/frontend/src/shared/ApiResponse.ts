/** 后端统一响应包裹 */
export interface ApiResult<T> {
  code: number;
  message: string;
  data: T;
}

/** 后端分页数据 */
export interface PaginatedData<T> {
  list: T[];
  total: number;
  page: number;
  pageSize: number;
}

/** 后端返回的原始 Todo 数据（API 契约） */
export interface TodoApiData {
  id: number;
  title: string;
  description: string;
  status: number;
  priority: number;
  dueDate: string | null;
  createdAt: string;
  updatedAt: string;
}
