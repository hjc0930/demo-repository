import {
  describe,
  expect,
  test,
  beforeAll,
  afterAll,
  afterEach,
  it,
  vi,
} from "vitest";
import { create } from "../src";
import { BASE_URL } from "./utils/constand";
import server, { resetHandlers } from "./utils/mock-http";

describe("Request configuration", () => {
  beforeAll(() => {
    server.listen();
  });
  afterAll(() => {
    server.close();
  });
  afterEach(() => {
    resetHandlers();
  });

  test("BaseUrl", async () => {
    const request = create({
      baseURL: BASE_URL,
    });

    const response = await request.get("/users");

    expect(response.data).toEqual([
      { id: 1, name: "Alice", email: "alice@example.com" },
      { id: 2, name: "Bob", email: "bob@example.com" },
    ]);
  });

  it("Timeout", async () => {
    const responseFn = vi.fn();
    const request = create({
      baseURL: BASE_URL,
      timeout: 1000,
    });

    request.interceptors.response.use(responseFn);

    try {
      await request.get("/auth/timeout");
    } catch (error) {
      expect(error?.name).toBe("Timeout");
      expect(responseFn).not.toHaveBeenCalled();
    }
  });
});
