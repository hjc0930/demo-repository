import { describe, it, expect, beforeEach, afterEach, afterAll } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { server } from "../../mocks/server";
import { TodoList } from "../TodoList";

describe("TodoList 组件", () => {
  beforeEach(() => {
    server.listen({ onUnhandledRequest: "error" });
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => {
    server.close();
  });

  it("应该正确渲染待办事项列表", async () => {
    render(<TodoList userId={1} />);

    await waitFor(() => {
      expect(screen.getByText("学习 React")).toBeInTheDocument();
      expect(screen.getByText("学习 TypeScript")).toBeInTheDocument();
    });
  });

  it("应该显示待办事项标题", () => {
    render(<TodoList userId={1} />);
    expect(screen.getByText("待办事项列表")).toBeInTheDocument();
  });

  it("应该能够添加新的待办事项", async () => {
    const user = userEvent.setup();
    render(<TodoList userId={1} />);

    const input = screen.getByPlaceholderText("输入新的待办事项");
    const addButton = screen.getByText("添加");

    await user.type(input, "新的待办事项");
    await user.click(addButton);

    await waitFor(() => {
      expect(screen.getByText("新的待办事项")).toBeInTheDocument();
      expect(input).toHaveValue("");
    });
  });

  it("空输入时添加应该显示警告", async () => {
    const user = userEvent.setup();
    render(<TodoList userId={1} />);

    const addButton = screen.getByText("添加");
    await user.click(addButton);

    await waitFor(() => {
      expect(screen.getByText("请输入待办事项内容")).toBeInTheDocument();
    });
  });

  it("按回车键应该添加待办事项", async () => {
    const user = userEvent.setup();
    render(<TodoList userId={1} />);

    const input = screen.getByPlaceholderText("输入新的待办事项");

    await user.type(input, "回车添加测试{Enter}");

    await waitFor(() => {
      expect(screen.getByText("回车添加测试")).toBeInTheDocument();
    });
  });

  it("应该能够切换待办事项完成状态", async () => {
    const user = userEvent.setup();
    render(<TodoList userId={1} />);

    await waitFor(() => {
      expect(screen.getByText("学习 React")).toBeInTheDocument();
    });

    const checkboxes = screen.getAllByRole("checkbox");
    await user.click(checkboxes[0]);

    await waitFor(() => {
      const todoTitle = screen.getByText("学习 React");
      expect(todoTitle).toHaveStyle({ textDecoration: "line-through" });
    });
  });

  it("已完成的待办事项应该有删除线样式", async () => {
    render(<TodoList userId={1} />);

    await waitFor(() => {
      const completedTodo = screen.getByText("学习 TypeScript");
      expect(completedTodo).toHaveStyle({ textDecoration: "line-through" });
    });
  });
});
