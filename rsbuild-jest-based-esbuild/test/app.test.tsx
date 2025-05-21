import App from "@/App";
import { render, screen } from "@testing-library/react";

test("App", () => {
  render(<App />);
  expect(screen.getByText("Rsbuild with React")).toBeInTheDocument();
});
