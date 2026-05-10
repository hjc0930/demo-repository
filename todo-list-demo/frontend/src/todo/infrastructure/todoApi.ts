import axios from "axios";
import type { ApiResult, PaginatedData, TodoApiData } from "@/shared/ApiResponse";

const http = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  timeout: 10000,
});

/** 待办事项 API（基础设施层） */
export const todoApi = {
  list(params: { status?: number; page?: number; pageSize?: number }) {
    return http.get<ApiResult<PaginatedData<TodoApiData>>>("/todos", {
      params,
    });
  },

  getById(id: number) {
    return http.get<ApiResult<TodoApiData>>(`/todos/${id}`);
  },

  create(dto: {
    title: string;
    description?: string;
    status?: number;
    priority?: number;
    dueDate?: string;
  }) {
    return http.post<ApiResult<TodoApiData>>("/todos", dto);
  },

  update(
    id: number,
    dto: Partial<{
      title: string;
      description: string;
      status: number;
      priority: number;
      dueDate: string | null;
    }>,
  ) {
    return http.put<ApiResult<TodoApiData>>(`/todos/${id}`, dto);
  },

  delete(id: number) {
    return http.delete<ApiResult<void>>(`/todos/${id}`);
  },
};
