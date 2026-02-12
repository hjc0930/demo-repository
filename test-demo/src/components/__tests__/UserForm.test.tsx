import { describe, it, expect, vi } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { UserForm } from "../UserForm";

describe("UserForm 组件", () => {
  it("应该正确渲染表单字段", () => {
    render(<UserForm onSubmit={vi.fn()} />);

    expect(screen.getByLabelText(/用户名/)).toBeInTheDocument();
    expect(screen.getByLabelText(/邮箱/)).toBeInTheDocument();
    expect(screen.getByLabelText(/密码/)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "提交" })).toBeInTheDocument();
  });

  it("应该验证必填字段", async () => {
    const user = userEvent.setup();
    render(<UserForm onSubmit={vi.fn()} />);

    const submitButton = screen.getByRole("button", { name: "提交" });
    await user.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText("请输入用户名")).toBeInTheDocument();
      expect(screen.getByText("请输入邮箱")).toBeInTheDocument();
      expect(screen.getByText("请输入密码")).toBeInTheDocument();
    });
  });

  it("应该验证邮箱格式", async () => {
    const user = userEvent.setup();
    render(<UserForm onSubmit={vi.fn()} />);

    const emailInput = screen.getByLabelText(/邮箱/);
    await user.type(emailInput, "invalid-email");

    const submitButton = screen.getByRole("button", { name: "提交" });
    await user.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText("请输入有效的邮箱地址")).toBeInTheDocument();
    });
  });

  it("应该验证用户名长度和格式", async () => {
    const user = userEvent.setup();
    render(<UserForm onSubmit={vi.fn()} />);

    const usernameInput = screen.getByLabelText(/用户名/);

    // 测试过短的用户名
    await user.type(usernameInput, "ab");
    await user.tab();

    await waitFor(() => {
      expect(screen.getByText("用户名长度为3-20个字符")).toBeInTheDocument();
    });

    // 清空并测试无效字符
    await user.clear(usernameInput);
    await user.type(usernameInput, "invalid username!");
    await user.tab();

    await waitFor(() => {
      expect(
        screen.getByText("用户名只能包含字母、数字和下划线"),
      ).toBeInTheDocument();
    });
  });

  it("应该验证密码最小长度", async () => {
    const user = userEvent.setup();
    render(<UserForm onSubmit={vi.fn()} />);

    const passwordInput = screen.getByLabelText(/密码/);
    await user.type(passwordInput, "12345");

    const submitButton = screen.getByRole("button", { name: "提交" });
    await user.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText("密码至少6个字符")).toBeInTheDocument();
    });
  });

  it("表单提交时应该调用 onSubmit", async () => {
    const user = userEvent.setup();
    const handleSubmit = vi.fn().mockResolvedValue(undefined);
    render(<UserForm onSubmit={handleSubmit} />);

    await user.type(screen.getByLabelText(/用户名/), "testuser");
    await user.type(screen.getByLabelText(/邮箱/), "test@example.com");
    await user.type(screen.getByLabelText(/密码/), "password123");

    const submitButton = screen.getByRole("button", { name: "提交" });
    await user.click(submitButton);

    await waitFor(() => {
      expect(handleSubmit).toHaveBeenCalledWith({
        username: "testuser",
        email: "test@example.com",
        password: "password123",
      });
    });
  });

  it("应该支持初始值", () => {
    render(
      <UserForm
        onSubmit={vi.fn()}
        initialValues={{ username: "existing", email: "existing@test.com" }}
        showPassword={false}
      />,
    );

    expect(screen.getByDisplayValue("existing")).toBeInTheDocument();
    expect(screen.getByDisplayValue("existing@test.com")).toBeInTheDocument();
    expect(screen.queryByLabelText(/密码/)).not.toBeInTheDocument();
  });

  it("编辑模式下密码不是必填的", () => {
    render(
      <UserForm
        onSubmit={vi.fn()}
        initialValues={{ username: "test", email: "test@example.com" }}
        showPassword={false}
      />,
    );

    expect(screen.queryByLabelText(/密码/)).not.toBeInTheDocument();
  });

  it("提交成功后应该重置表单（新增模式）", async () => {
    const user = userEvent.setup();
    const handleSubmit = vi.fn().mockResolvedValue(undefined);
    render(<UserForm onSubmit={handleSubmit} />);

    await user.type(screen.getByLabelText(/用户名/), "testuser");
    await user.type(screen.getByLabelText(/邮箱/), "test@example.com");
    await user.type(screen.getByLabelText(/密码/), "password123");

    const submitButton = screen.getByRole("button", { name: "提交" });
    await user.click(submitButton);

    await waitFor(() => {
      expect(screen.getByLabelText(/用户名/)).toHaveValue("");
    });
  });
});
