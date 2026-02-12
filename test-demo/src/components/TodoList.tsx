import { useEffect, useState } from "react";
import {
  Checkbox,
  Button,
  Input,
  Space,
  Card,
  message,
  Popconfirm,
  Flex,
  Spin,
} from "antd";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import type { Todo } from "../types";
import {
  getTodoList,
  createTodo,
  deleteTodo,
  toggleTodo,
} from "../services/todoService";

interface TodoListProps {
  userId?: number;
}

export const TodoList: React.FC<TodoListProps> = ({ userId = 1 }) => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [loading, setLoading] = useState(false);
  const [inputValue, setInputValue] = useState("");

  const fetchTodos = async () => {
    try {
      setLoading(true);
      const response = (await getTodoList({ page: 1, pageSize: 100 })) as any;
      setTodos(response.data || []);
    } catch {
      message.error("获取待办事项失败");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTodos();
  }, [userId]);

  const handleAdd = async () => {
    if (!inputValue.trim()) {
      message.warning("请输入待办事项内容");
      return;
    }

    try {
      const response = (await createTodo({
        title: inputValue,
        completed: false,
        userId,
      })) as any;
      setTodos((prev) => [...prev, response.data]);
      setInputValue("");
      message.success("添加成功");
    } catch {
      message.error("添加失败");
    }
  };

  const handleToggle = async (id: number, completed: boolean) => {
    try {
      const response = (await toggleTodo(id, !completed)) as any;
      setTodos((prev) =>
        prev.map((todo) => (todo.id === id ? response.data : todo)),
      );
    } catch {
      message.error("更新失败");
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteTodo(id);
      setTodos((prev) => prev.filter((todo) => todo.id !== id));
      message.success("删除成功");
    } catch {
      message.error("删除失败");
    }
  };

  return (
    <Card title="待办事项列表" style={{ maxWidth: 600 }}>
      <Space.Compact style={{ width: "100%", marginBottom: 16 }}>
        <Input
          placeholder="输入新的待办事项"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onPressEnter={handleAdd}
        />
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          添加
        </Button>
      </Space.Compact>

      <Spin spinning={loading}>
        <Space orientation="vertical" style={{ width: "100%" }} size="middle">
          {todos.map((todo) => (
            <Flex
              key={todo.id}
              align="center"
              justify="space-between"
              style={{
                padding: "12px",
                border: "1px solid #f0f0f0",
                borderRadius: "8px",
                backgroundColor: "#fff",
              }}
            >
              <Flex align="center" gap="middle">
                <Checkbox
                  checked={todo.completed}
                  onChange={() => handleToggle(todo.id, todo.completed)}
                />
                <span
                  style={{
                    textDecoration: todo.completed ? "line-through" : "none",
                    color: todo.completed ? "#999" : "inherit",
                  }}
                >
                  {todo.title}
                </span>
              </Flex>

              <Popconfirm
                title="确定要删除这个待办事项吗?"
                onConfirm={() => handleDelete(todo.id)}
                okText="确定"
                cancelText="取消"
              >
                <Button
                  type="text"
                  danger
                  icon={<DeleteOutlined />}
                  data-testid="delete"
                />
              </Popconfirm>
            </Flex>
          ))}
        </Space>
      </Spin>
    </Card>
  );
};

export default TodoList;
