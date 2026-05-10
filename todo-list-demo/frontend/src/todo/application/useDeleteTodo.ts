import { useState, useCallback } from "react";
import { todoApi } from "../infrastructure/todoApi";

interface UseDeleteTodoReturn {
  loading: boolean;
  error: string | null;
  remove: (id: number) => Promise<boolean>;
}

/**
 * 应用服务：编排删除待办流程
 */
export function useDeleteTodo(): UseDeleteTodoReturn {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const remove = useCallback(async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      await todoApi.delete(id);
      return true;
    } catch (e) {
      setError(e instanceof Error ? e.message : "删除失败");
      return false;
    } finally {
      setLoading(false);
    }
  }, []);

  return { loading, error, remove };
}
