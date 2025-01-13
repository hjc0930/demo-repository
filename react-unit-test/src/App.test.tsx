import { render, screen, renderHook, cleanup } from "@testing-library/react";
import App from "./App";
import useCount from "./hooks/useCount";
import { act } from "react-dom/test-utils";
import useFetch from "./hooks/useFetch";

afterEach(cleanup);

describe("App", () => {
  test("renders learn react link", () => {
    render(<App />);
    // const linkElement = screen.getByText(/learn react/i);
    const linkElement = screen.getByRole("link", { name: /learn react/i });
    expect(linkElement).toBeInTheDocument();
  });

  test("useCount", () => {
    const { result } = renderHook(() => useCount());

    act(() => {
      result.current.increment();
    });

    expect(result.current.count).toBe(1);

    act(() => {
      result.current.decrement();
    });

    expect(result.current.count).toBe(0);
  });

  test("useFetch", async () => {
    const url = "https://jsonplaceholder.typicode.com/posts/1";

    const { result } = renderHook(() => useFetch(url));

    expect(result.current.data).toBeNull();
    expect(result.current.loading).toBeTruthy();
    expect(result.current.error).toBeNull();

    await result.current.fetchData();

    await act(async () => {
      await result.current.fetchData();
    });

    expect(result.current.data).toEqual({
      userId: 1,
      id: 1,
      title:
        "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
      body: "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
    });
  });
});
