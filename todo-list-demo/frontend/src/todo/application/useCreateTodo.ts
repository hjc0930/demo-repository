import { useState, useCallback } from "react";
import { Todo } from "../domain/Todo";
import { todoApi } from "../infrastructure/todoApi";

interface UseCreateTodoReturn {
  loading: boolean;
  error: string | null;
  create: (input: {
    title: string;
    description?: string;
    priority?: number;
    dueDate?: string;
  }) => Promise<Todo | null>;
}

/**
 * 应用服务：编排创建待办流程
 */
export function useCreateTodo(): UseCreateTodoReturn {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const create = useCallback(
    async (input: {
      title: string;
      description?: string;
      priority?: number;
      dueDate?: string;
    }) => {
      setLoading(true);
      setError(null);
      try {
        const dto = Todo.create(input); // 委托给领域层
        const res = await todoApi.create(dto);
        return Todo.fromApi(res.data.data);
      } catch (e) {
        const msg = e instanceof Error ? e.message : "创建失败";
        setError(msg);
        return null;
      } finally {
        setLoading(false);
      }
    },
    [],
  );

  return { loading, error, create };
}
