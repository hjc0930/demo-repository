import { render, screen } from "@testing-library/react";
import App from "./App";

describe("Demo", () => {
  it("renders Dropdown", () => {
    render(<App />);
    expect(screen.getByText(/hello world/i)).toBeInTheDocument();
  });

  it("Add", () => {
    expect(1 + 3).toBe(4);
  });
});
