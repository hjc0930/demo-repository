import { render, screen, waitFor } from "@testing-library/react";
import { Dropdown } from "antd";
import userEvent from "@testing-library/user-event";
import App from "./App";

describe("Demo", () => {
  it("renders Dropdown", async () => {
    render(<App />);
    expect(screen.getByText(/hello world/i)).toBeInTheDocument();
    // render(
    //   <Dropdown
    //     menu={{
    //       items: [{ key: "1", label: <span>123123</span> }],
    //     }}
    //   >
    //     <button>Click me</button>
    //   </Dropdown>
    // );
    // const triggerElement = screen.getByRole("button", { name: /Click me/i });

    // await userEvent.hover(triggerElement);
    // await waitFor(() => {
    //   expect(screen.getByText(/123123/i)).toBeInTheDocument();
    // });
  });

  it("Add", () => {
    expect(1 + 3).toBe(4);
  });
});
