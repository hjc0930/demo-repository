import {
  describe,
  it,
  expect,
  beforeAll,
  afterAll,
  afterEach,
  vi,
} from "vitest";
import { create } from "../src";
import server, { resetHandlers } from "./utils/mock-http";
import { BASE_URL } from "./utils/constand";
import { RequestError } from "../src/utils";

describe("Request interceptor", () => {
  beforeAll(() => {
    server.listen();
  });
  afterAll(() => {
    server.close();
  });
  afterEach(() => {
    resetHandlers();
  });

  it("Request interceptor", async () => {
    const request = create();
    request.interceptors.request.use((context) => {
      context.headers?.set("Token", "Bearer valid_token");
      return context;
    });

    const response = await request.post(BASE_URL + "/auth");

    expect(response.response.status).toBe(200);
  });

  it("Response interceptor", async () => {
    const request = create();

    request.interceptors.response.use((context) => {
      return context.data;
    });

    const response = await request.get(BASE_URL + "/users");
    expect(response).toEqual([
      {
        id: 1,
        name: "Alice",
        email: "alice@example.com",
      },
      {
        id: 2,
        name: "Bob",
        email: "bob@example.com",
      },
    ]);
  });

  it("Request interceptor error", async () => {
    const onFirstRejectFn = vi.fn();
    const onRejectFn = vi.fn();
    const request = create();

    request.interceptors.request.use(() => {
      throw new RequestError("Request error", "Request error");
    }, onFirstRejectFn);
    request.interceptors.request.use(undefined, onRejectFn);

    try {
      await request.get(BASE_URL + "/users");
    } catch (error) {
      expect(onFirstRejectFn).toHaveBeenCalledTimes(0);
      expect(onRejectFn).toHaveBeenCalledTimes(1);
    }
  });

  it("Response interceptor error", async () => {
    const request = create();
    request.interceptors.response.use(
      (context) => {
        return context.data;
      },
      (context) => {
        return context?.response;
      }
    );
    const response = await request.post(BASE_URL + "/auth");
    expect(response.status).toBe(401);
  });

  it("Remove interceptor", async () => {
    const fn = vi.fn((context) => context);
    const request = create();
    const interceptorId = request.interceptors.request.use(fn);
    await request.get(BASE_URL);
    expect(fn).toHaveBeenCalledTimes(1);

    fn.mockReset();
    request.interceptors.request.remove(interceptorId);
    await request.get(BASE_URL);
    expect(fn).toHaveBeenCalledTimes(0);
  });
});
