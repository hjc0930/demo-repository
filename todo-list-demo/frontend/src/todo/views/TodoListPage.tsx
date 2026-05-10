import { useEffect, useState, useCallback } from "react";
import {
  Container,
  Box,
  Stack,
  Typography,
  Pagination,
  Fab,
  CircularProgress,
  Alert,
} from "@mui/material";
import { Add } from "@mui/icons-material";
import { Todo } from "../domain/Todo";
import { TodoStatus } from "../domain/TodoStatus";
import { useTodoList } from "../application/useTodoList";
import { useCreateTodo } from "../application/useCreateTodo";
import { useUpdateTodo } from "../application/useUpdateTodo";
import { useDeleteTodo } from "../application/useDeleteTodo";
import { TodoCard } from "./TodoCard";
import { TodoFilter } from "./TodoFilter";
import { TodoFormDialog } from "./TodoFormDialog";

/**
 * UI 页面：待办列表
 * 编排子组件和应用服务
 */
export function TodoListPage() {
  // ===== 状态 =====
  const [statusFilter, setStatusFilter] = useState<TodoStatus | null>(null);
  const [page, setPage] = useState(1);
  const [formOpen, setFormOpen] = useState(false);
  const [editingTodo, setEditingTodo] = useState<Todo | null>(null);

  // ===== 应用服务 =====
  const { list, loading, error, fetch } = useTodoList();
  const { create } = useCreateTodo();
  const { complete, update, error: updateError } = useUpdateTodo();
  const { remove } = useDeleteTodo();

  // 初始加载和筛选/翻页时重新加载
  useEffect(() => {
    fetch({
      status: statusFilter?.value,
      page,
      pageSize: 10,
    });
  }, [statusFilter, page, fetch]);

  // ===== 事件处理（编排，不含业务逻辑） =====
  const handleComplete = useCallback(
    async (todo: Todo) => {
      const updated = await complete(todo);
      if (updated && list) {
        list.updateItem(updated);
        fetch({ status: statusFilter?.value, page, pageSize: 10 });
      }
    },
    [complete, list, statusFilter, page, fetch],
  );

  const handleStartProgress = useCallback(
    async (todo: Todo) => {
      const updated = await update(todo.id, { status: TodoStatus.IN_PROGRESS.value });
      if (updated && list) {
        list.updateItem(updated);
        fetch({ status: statusFilter?.value, page, pageSize: 10 });
      }
    },
    [update, list, statusFilter, page, fetch],
  );

  const handleEdit = useCallback((todo: Todo) => {
    setEditingTodo(todo);
    setFormOpen(true);
  }, []);

  const handleDelete = useCallback(
    async (todo: Todo) => {
      const ok = await remove(todo.id);
      if (ok && list) {
        list.removeItem(todo.id);
        fetch({ status: statusFilter?.value, page, pageSize: 10 });
      }
    },
    [remove, list, statusFilter, page, fetch],
  );

  const handleCreate = useCallback(() => {
    setEditingTodo(null);
    setFormOpen(true);
  }, []);

  const handleFormSubmit = useCallback(
    async (payload: {
      title: string;
      description: string;
      priority: number;
      dueDate: string;
    }) => {
      if (editingTodo) {
        await update(editingTodo.id, {
          title: payload.title,
          description: payload.description,
          priority: payload.priority,
          dueDate: payload.dueDate || null,
        });
      } else {
        await create(payload);
      }
      fetch({ status: statusFilter?.value, page, pageSize: 10 });
    },
    [editingTodo, create, update, statusFilter, page, fetch],
  );

  // ===== 渲染 =====
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      {/* 标题 */}
      <Typography variant="h4" gutterBottom>
        待办事项
      </Typography>

      {/* 筛选和操作 */}
      <Stack
        direction="row"
        sx={{ justifyContent: "space-between", alignItems: "center", mb: 3 }}
      >
        <TodoFilter current={statusFilter} onChange={setStatusFilter} />
      </Stack>

      {/* 错误信息 */}
      {(error || updateError) && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error || updateError}
        </Alert>
      )}

      {/* 加载中 */}
      {loading && (
        <Box sx={{ textAlign: "center", py: 4 }}>
          <CircularProgress />
        </Box>
      )}

      {/* 空状态 */}
      {!loading && list?.isEmpty && (
        <Box sx={{ textAlign: "center", py: 6 }}>
          <Typography variant="h6" color="text.secondary">
            暂无待办事项
          </Typography>
          <Typography variant="body2" color="text.disabled" sx={{ mt: 1 }}>
            点击右下角 + 按钮创建第一条待办
          </Typography>
        </Box>
      )}

      {/* 待办列表 */}
      {!loading && list && (
        <>
          <Stack spacing={2}>
            {list.items.map((todo) => (
              <TodoCard
                key={todo.id}
                todo={todo}
                onComplete={handleComplete}
                onStartProgress={handleStartProgress}
                onEdit={handleEdit}
                onDelete={handleDelete}
              />
            ))}
          </Stack>

          {/* 分页 */}
          {list.total > 10 && (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 3 }}>
              <Pagination
                count={Math.ceil(list.total / list.pageSize)}
                page={list.page}
                onChange={(_, p) => setPage(p)}
                color="primary"
              />
            </Box>
          )}
        </>
      )}

      {/* 创建 FAB */}
      <Fab
        color="primary"
        sx={{ position: "fixed", bottom: 24, right: 24 }}
        onClick={handleCreate}
      >
        <Add />
      </Fab>

      {/* 创建/编辑弹窗 */}
      <TodoFormDialog
        open={formOpen}
        editTodo={editingTodo}
        onClose={() => setFormOpen(false)}
        onSubmit={handleFormSubmit}
      />
    </Container>
  );
}
