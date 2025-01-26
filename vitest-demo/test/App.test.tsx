import { fireEvent, render, screen } from "@testing-library/react";
import App from "@/App";
import { describe, expect, test, vi } from "vitest";
import { act } from "react";

describe("App Component", () => {
  test("renders the app correctly", async () => {
    render(<App />);
    const linkElement = screen.getByText(
      /Click on the Vite and React logos to learn more/i
    );
    await act(() => Promise.resolve());
    expect(linkElement).toBeInTheDocument();
  });
  test("Upload case", async () => {
    const root = render(<App />);
    const inputElement = root.container.querySelector(
      "input[type='file']"
    ) as HTMLInputElement;

    const file = new File(["test"], "test.png", { type: "image/png" });
    fireEvent.change(inputElement, {
      target: {
        files: [file],
      },
    });
    await act(() => Promise.resolve());
    expect(inputElement?.files?.[0]).toEqual(file);
    expect(inputElement?.files).toHaveLength(1);
  });
  test("renders about page", async () => {
    vi.mock("@/services/app.ts", () => ({
      getAppData: vi.fn(() =>
        Promise.resolve([
          {
            id: 1,
            name: "App 233",
          },
        ])
      ),
    }));
    render(<App />);
    await act(() => Promise.resolve());
    await vi.waitFor(() => {
      expect(screen.getByText(/App 233/i)).toBeInTheDocument();
    });
  });
  test("Dropdown case", async () => {
    render(<App />);

    fireEvent.mouseEnter(screen.getByText(/Dropdown Click/i));
    await vi.waitFor(() => {
      expect(screen.getByText(/Dropdown First Element/i)).toBeInTheDocument();
    });
  });
});
