import {
  act,
  fireEvent,
  render,
  screen,
  waitFor,
} from "@testing-library/react";
import { useRequest } from "ahooks";
import Demo from "../Demo";

jest.mock("ahooks");

test("Demo", async () => {
  const fn = jest.fn();
  fn.mockResolvedValue("Faild");
  jest.mocked(useRequest).mockReturnValue({
    runAsync: fn,
  } as any);
  render(<Demo />);

  act(() => {
    fireEvent.click(screen.getByRole("button", { name: "Click" }));
  });

  expect(fn).toHaveBeenCalled();
  expect(fn).toHaveBeenCalledWith(1, 2);
  await waitFor(() => {
    expect(screen.getByText("Faild")).toBeInTheDocument();
  });
});
