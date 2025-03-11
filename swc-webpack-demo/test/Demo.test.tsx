import { render } from "@testing-library/react";

const App = () => {
  return <div>123213</div>;
};

describe("Demo", () => {
  test("Demo", () => {
    expect(1 + 2).toBe(3);
  });

  test("App", () => {
    render(<App />);
  });
});
