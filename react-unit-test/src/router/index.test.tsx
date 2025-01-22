import { render, screen } from "@testing-library/react";
import Router from "./index";

describe("Router Component", () => {
  test("renders without crashing", () => {
    render(<Router />);
  });

  test('renders Home heading on "/" path', () => {
    render(<Router />);
    const heading = screen.getByRole("heading", { name: /home/i });
    expect(heading).toBeInTheDocument();
  });

  // test('renders About heading on "/about" path', () => {
  //   render(
  //     <MemoryRouter initialIndex={1}>
  //       <Router />
  //     </MemoryRouter>
  //   );
  //   const heading = screen.getByRole("heading", { name: /about/i });
  //   expect(heading).toBeInTheDocument();
  // });
});
