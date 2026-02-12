import {
  describe,
  it,
  expect,
  vi,
  beforeEach,
  afterEach,
  afterAll,
} from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { server } from "../../../mocks/server";
import { UserList } from "../UserList";
import { http, HttpResponse } from "msw";

describe("UserList 组件", () => {
  const onEdit = vi.fn();

  beforeEach(() => {
    server.listen({ onUnhandledRequest: "error" });
  });

  afterEach(() => {
    server.resetHandlers();
    vi.clearAllMocks();
  });

  afterAll(() => {
    server.close();
  });

  it("应该正确渲染用户列表", async () => {
    render(<UserList onEdit={onEdit} />);

    await waitFor(() => {
      expect(screen.getByText("张三")).toBeInTheDocument();
      expect(screen.getByText("李四")).toBeInTheDocument();
    });
  });

  it("应该显示用户表格列", async () => {
    render(<UserList onEdit={onEdit} />);

    await waitFor(() => {
      expect(screen.getByText("ID")).toBeInTheDocument();
      expect(screen.getByText("用户名")).toBeInTheDocument();
      expect(screen.getByText("邮箱")).toBeInTheDocument();
      expect(screen.getByText("角色")).toBeInTheDocument();
      expect(screen.getByText("操作")).toBeInTheDocument();
    });
  });

  it("管理员角色应该显示为红色标签", async () => {
    render(<UserList onEdit={onEdit} />);

    await waitFor(() => {
      const adminTag = screen.getByText("管理员");
      expect(adminTag).toBeInTheDocument();
      // Ant Design 6.x 的 Tag 组件使用不同的类名
      expect(adminTag.closest(".ant-tag")).toBeInTheDocument();
    });
  });

  it("普通用户角色应该显示为蓝色标签", async () => {
    render(<UserList onEdit={onEdit} />);

    await waitFor(() => {
      const userTag = screen.getByText("普通用户");
      expect(userTag).toBeInTheDocument();
    });
  });

  it("点击编辑按钮应该调用 onEdit 回调", async () => {
    const user = userEvent.setup();
    render(<UserList onEdit={onEdit} />);

    await waitFor(() => {
      expect(screen.getByText("张三")).toBeInTheDocument();
    });

    const editButtons = screen.getAllByText("编辑");
    await user.click(editButtons[0]);

    await waitFor(() => {
      expect(onEdit).toHaveBeenCalled();
    });
  });

  it("应该支持 refreshTrigger 触发刷新", async () => {
    const { rerender } = render(
      <UserList onEdit={onEdit} refreshTrigger={0} />,
    );

    await waitFor(() => {
      expect(screen.getByText("张三")).toBeInTheDocument();
    });

    rerender(<UserList onEdit={onEdit} refreshTrigger={1} />);

    await waitFor(() => {
      expect(screen.getByText("张三")).toBeInTheDocument();
    });
  });

  it("请求失败时应该显示错误提示", async () => {
    server.use(
      http.get("/api/user/list", () => {
        return HttpResponse.json(
          { code: 500, message: "服务器错误", data: null },
          { status: 500 },
        );
      }),
    );

    render(<UserList onEdit={onEdit} />);

    await waitFor(() => {
      expect(screen.getByText("获取用户列表失败")).toBeInTheDocument();
    });
  });
});
