import { render } from "@testing-library/react";
import App from "@/App";
import { getEnvironment, getEnvironmentVariable } from "./a";

process.env.PREFIX = "/aaa";

describe("Demo", () => {
  test("App", () => {
    const { container } = render(<App />);
    expect(container).toMatchSnapshot();
  });

  test("getEnvironment", async () => {
    const result = getEnvironment();
    expect(result).toBe("/aaa");
  });

  test("getEnvironmentVariable", () => {
    const resultVariable = getEnvironmentVariable();
    expect(resultVariable).toBe("/aaa");
  });
});
