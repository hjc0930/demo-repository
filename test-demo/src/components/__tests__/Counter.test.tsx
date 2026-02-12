import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { Counter } from "@/components/Counter";

describe("Counter Component", () => {
  it("Should render initial value correctly", () => {
    render(<Counter initialValue={5} />);
    expect(screen.getByText("5")).toBeInTheDocument();
  });

  it("Should increment correctly", async () => {
    const user = userEvent.setup();
    render(<Counter initialValue={0} />);
    const incrementButton = screen.getByText("+");

    await user.click(incrementButton);
    expect(screen.getByText("1")).toBeInTheDocument();

    await user.click(incrementButton);
    expect(screen.getByText("2")).toBeInTheDocument();
  });

  it("Should decrement correctly", async () => {
    const user = userEvent.setup();
    render(<Counter initialValue={5} />);
    const decrementButton = screen.getByText("-");

    await user.click(decrementButton);
    expect(screen.getByText("4")).toBeInTheDocument();
  });

  it("Should reset to initial value", async () => {
    const user = userEvent.setup();
    render(<Counter initialValue={10} />);
    const incrementButton = screen.getByText("+");
    const resetButton = screen.getByText("重 置");

    await user.click(incrementButton);
    expect(screen.getByText("11")).toBeInTheDocument();

    await user.click(resetButton);
    await waitFor(() => {
      expect(screen.getByText("10")).toBeInTheDocument();
    });
  });

  it("Should respect max value limit", async () => {
    const user = userEvent.setup();
    render(<Counter initialValue={9} max={10} />);
    const incrementButton = screen.getByRole("button", { name: "+" });

    await user.click(incrementButton);

    expect(screen.getByText("10")).toBeInTheDocument();
    expect(incrementButton).toBeDisabled();
  });

  it("Should respect min value limit", async () => {
    const user = userEvent.setup();
    render(<Counter initialValue={1} min={0} />);
    const decrementButton = screen.getByRole("button", { name: "-" });

    await user.click(decrementButton);
    await waitFor(() => {
      expect(screen.getByText("0")).toBeInTheDocument();
    });

    expect(decrementButton).toBeDisabled();
  });

  it("Should call callback function when value changes", async () => {
    const user = userEvent.setup();
    const onValueChange = vi.fn();
    render(<Counter initialValue={0} onValueChange={onValueChange} />);

    const incrementButton = screen.getByText("+");
    await user.click(incrementButton);

    expect(onValueChange).toHaveBeenCalledWith(1);
    expect(onValueChange).toHaveBeenCalledTimes(1);
  });

  it("Should display counter title", () => {
    render(<Counter />);
    expect(screen.getByText("计数器")).toBeInTheDocument();
  });
});
