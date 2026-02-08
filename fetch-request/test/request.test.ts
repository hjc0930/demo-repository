import { describe, it, expect, beforeAll, afterAll, afterEach } from "vitest";
import request from "../src";
import server, { resetHandlers } from "./utils/mock-http";
import { Method } from "../src/types";
import { BASE_URL } from "./utils/constand";

describe("Base request", () => {
  beforeAll(() => {
    server.listen();
  });
  afterAll(() => {
    server.close();
  });
  afterEach(() => {
    resetHandlers();
  });

  it("Base request for get method", async () => {
    const response = await request({
      url: BASE_URL,
    });

    expect(response.data.status).toBe(200);
    expect(response.data.data).toEqual([
      {
        userId: 1,
        id: 1,
        title: "first post title",
        body: "first post body",
      },
    ]);
  });

  it.each(["get", "post", "put", "patch", "delete", "head", "options"])(
    "Test call %s method",
    async (method: Method) => {
      expect(request[method]).toBeDefined();
      expect(request[method]).toBeInstanceOf(Function);
      const response = await request({
        url: BASE_URL,
        method,
      });
      expect(response.data.data).toEqual([
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ]);
    }
  );

  it.each(["get", "post", "put", "patch", "delete", "head", "options"])(
    "Test simple %s method",
    async (method: Method) => {
      expect(request[method]).toBeDefined();
      expect(request[method]).toBeInstanceOf(Function);
      const response = await request[method](BASE_URL);
      expect(response.data.data).toEqual([
        {
          userId: 1,
          id: 1,
          title: "first post title",
          body: "first post body",
        },
      ]);
    }
  );
});
