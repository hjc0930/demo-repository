import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import App from "../App";

// 模拟 Button 组件
jest.mock("@/components/Button", () => ({
  Button: ({ children }: { children: React.ReactNode }) => (
    <button data-testid="mocked-button">{children}</button>
  ),
}));

describe("App", () => {
  it("renders main heading", () => {
    render(<App />);
    const headingElement = screen.getByText(/Rsbuild with React/i);
    expect(headingElement).toBeInTheDocument();
  });

  it("renders welcome message", () => {
    render(<App />);
    const messageElement = screen.getByText(
      /Start building amazing things with Rsbuild/i
    );
    expect(messageElement).toBeInTheDocument();
  });

  it("renders button with correct text", () => {
    render(<App />);

    // 验证按钮渲染
    const buttonElement = screen.getByTestId("mocked-button");
    expect(buttonElement).toBeInTheDocument();
    expect(buttonElement).toHaveTextContent("123123");
  });

  test("Dropdown", async () => {
    render(<App />);
    const triggerEl = screen.getByText(/Dropdown Trigger/i);
    fireEvent.mouseEnter(triggerEl);

    await waitFor(() => {
      expect(screen.getByText(/Menu Click/i)).toBeInTheDocument();
    });
  });
});
