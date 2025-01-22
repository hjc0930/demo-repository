import { render, screen } from "@testing-library/react";
import App from "./App";
import { describe, expect, test } from "vitest";

describe("App Component", () => {
  test("renders the app correctly", () => {
    render(<App />);
    const linkElement = screen.getByText(
      /Click on the Vite and React logos to learn more/i
    ); // Adjust based on actual content
    expect(linkElement).toBeInTheDocument();
  });

  // Add more tests as needed
});
