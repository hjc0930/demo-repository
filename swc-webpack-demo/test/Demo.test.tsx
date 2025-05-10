import { render } from "@testing-library/react";
import App from "@/App";

describe("Demo", () => {
  test("App", () => {
    const { container } = render(<App />);
    expect(container).toMatchSnapshot();
    expect(container.querySelector("div")?.textContent).toBe("1");
  });
});
