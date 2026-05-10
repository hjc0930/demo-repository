import { useState, useCallback } from "react";
import { TodoList } from "../domain/TodoList";
import { todoApi } from "../infrastructure/todoApi";

interface UseTodoListReturn {
  list: TodoList | null;
  loading: boolean;
  error: string | null;
  fetch: (params: { status?: number; page?: number; pageSize?: number }) => Promise<void>;
}

/**
 * 应用服务：编排待办列表查询
 * 不含业务规则 —— 业务规则在领域层
 */
export function useTodoList(): UseTodoListReturn {
  const [list, setList] = useState<TodoList | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetch = useCallback(
    async (params: { status?: number; page?: number; pageSize?: number }) => {
      setLoading(true);
      setError(null);
      try {
        const res = await todoApi.list(params);
        setList(TodoList.fromApi(res.data.data)); // 委托给领域层转换
      } catch (e) {
        setError(e instanceof Error ? e.message : "加载失败");
      } finally {
        setLoading(false);
      }
    },
    [],
  );

  return { list, loading, error, fetch };
}
