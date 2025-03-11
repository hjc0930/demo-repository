import { fireEvent, render, screen } from "@testing-library/react";
import { useRequest } from "ahooks";

vi.mock("ahooks", { spy: true });

const Demo = () => {
  const { runAsync } = useRequest(async () => "", { manual: true });
  const inlineFn = async () => {
    await runAsync();
  };
  return <button onClick={inlineFn}>Click</button>;
};

test("Demo", async () => {
  const fn = vi.fn();
  vi.mocked(useRequest).mockReturnValue({
    runAsync: fn,
  } as any);
  fn.mockRejectedValue("Faild");
  render(<Demo />);

  fireEvent.click(screen.getByRole("button", { name: "Click" }));

  await expect(fn).rejects.toThrow("Faild");
});
