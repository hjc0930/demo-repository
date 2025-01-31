import { render, screen } from "@testing-library/react";
import routerConfig from "../src/router/config"; // 假设 index.tsx 文件导出 Router
import { createMemoryRouter, RouterProvider } from "react-router-dom";
import { userEvent } from "@testing-library/user-event";
import { useState } from "react";

describe("Router", () => {
  test("renders home page", () => {
    const homeMemoryRouter = createMemoryRouter(routerConfig, {
      initialEntries: ["/"],
    });
    render(<RouterProvider router={homeMemoryRouter} />);
    expect(screen.getByText(/Home/i)).toBeInTheDocument();
  });

  test("renders about page", async () => {
    const aboutMemoryRouter = createMemoryRouter(routerConfig, {
      initialEntries: ["/about"],
    });

    render(<RouterProvider router={aboutMemoryRouter} />);
    expect(screen.getByText(/about/i)).toBeInTheDocument();
  });

  test("Change Demo", async () => {
    const Demo = () => {
      const [count, setCount] = useState(0);

      return (
        <div>
          <p>{count}</p>
          <button onClick={() => setCount(count + 1)}>Click</button>
        </div>
      );
    };
    render(<Demo />);

    await userEvent.click(screen.getByText(/Click/i));

    await vi.waitFor(() => {
      expect(screen.getByText(/1/i)).not.toBeNull();
    });
  });
});
