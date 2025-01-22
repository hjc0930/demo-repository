import { render, screen } from "@testing-library/react";
import { routerConfig } from "./index"; // 假设 index.tsx 文件导出 Router
import { describe, expect, test, vi } from "vitest";
import { createMemoryRouter, RouterProvider } from "react-router-dom";

describe("Router", () => {
  test("renders home page", () => {
    const homeMemoryRouter = createMemoryRouter(routerConfig, {
      initialEntries: ["/"],
    });
    render(<RouterProvider router={homeMemoryRouter} />);
    expect(screen.getByText(/Home/i)).not.toBeNull();
  });

  test("renders about page", () => {
    const aboutMemoryRouter = createMemoryRouter(routerConfig, {
      initialEntries: ["/about"],
    });

    render(<RouterProvider router={aboutMemoryRouter} />);
    vi.waitFor(() => {
      expect(screen.getByText(/About/i)).not.toBeNull();
    });
  });
});
