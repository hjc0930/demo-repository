import { render } from "@testing-library/react";
import App from "./App";

describe("index.tsx Tests", () => {
  it("renders App component without crashing", () => {
    const { container } = render(<App />);
    expect(container).toBeInTheDocument();
  });
});
