import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import App from "./App";
import { Button, Dropdown } from "antd";

describe("App", () => {
  it("renders learn react link", () => {
    render(<App />);
    const textElement = screen.getByText(/rsbuild with react/i);
    expect(textElement).toBeInTheDocument();
  });

  test("Dropdown", async () => {
    render(
      <Dropdown menu={{ items: [{ label: <>Dropdown Item</>, key: 1 }] }}>
        <Button>Dropdown Click</Button>
      </Dropdown>
    );
    const triggerElement = screen.getByText(/dropdown click/i);
    fireEvent.mouseEnter(triggerElement);

    await waitFor(() => {
      expect(screen.getByText(/dropdown item/i)).toBeInTheDocument();
    });
  });

  test("Button", async () => {
    const fn = jest.fn();
    render(<Button onClick={fn}>Click Me</Button>);
    const triggerEl = screen.getByText(/click me/i);
    fireEvent.click(triggerEl);

    expect(fn).toHaveBeenCalled();
  });
});
