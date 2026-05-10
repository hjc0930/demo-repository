import { useState, useCallback } from "react";
import { Todo } from "../domain/Todo";
import { DomainError } from "@/shared/DomainError";
import { todoApi } from "../infrastructure/todoApi";

interface UseUpdateTodoReturn {
  loading: boolean;
  error: string | null;
  /** 完成待办 */
  complete: (todo: Todo) => Promise<Todo | null>;
  /** 部分更新 */
  update: (id: number, dto: Record<string, unknown>) => Promise<Todo | null>;
}

/**
 * 应用服务：编排更新待办流程
 * 领域规则（如 complete 检查）在 Todo.complete() 中执行
 */
export function useUpdateTodo(): UseUpdateTodoReturn {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const complete = useCallback(async (todo: Todo) => {
    setLoading(true);
    setError(null);
    try {
      todo.complete(); // 领域规则校验，DomainError 时这里抛出
      const res = await todoApi.update(todo.id, { status: todo.status.value });
      return Todo.fromApi(res.data.data);
    } catch (e) {
      if (e instanceof DomainError) {
        setError(e.message);
      } else {
        setError("操作失败");
      }
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const update = useCallback(async (id: number, dto: Record<string, unknown>) => {
    setLoading(true);
    setError(null);
    try {
      const res = await todoApi.update(id, dto);
      return Todo.fromApi(res.data.data);
    } catch (e) {
      setError(e instanceof Error ? e.message : "更新失败");
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  return { loading, error, complete, update };
}
