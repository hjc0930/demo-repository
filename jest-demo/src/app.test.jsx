import { render, screen, waitFor } from "@testing-library/react";
import { Dropdown } from "antd";
import userEvent from "@testing-library/user-event";

describe("Demo", () => {
  it("renders Dropdown", async () => {
    render(
      <Dropdown
        menu={{
          items: [{ key: "1", label: <span>123123</span> }],
        }}
      >
        <button>Click me</button>
      </Dropdown>
    );
    const triggerElement = screen.getByRole("button", { name: /Click me/i });

    await userEvent.hover(triggerElement);
    await waitFor(() => {
      expect(screen.getByText(/123123/i)).toBeInTheDocument();
    });
  });

  it("Add", () => {
    expect(1 + 3).toBe(4);
  });
});
