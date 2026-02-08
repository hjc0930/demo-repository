import { describe, it, expect, beforeAll, afterAll, afterEach } from "vitest";
import request from "../src";
import server, { resetHandlers } from "./utils/mock-http";
import { BASE_URL } from "./utils/constand";

describe("Request with params", () => {
  afterEach(() => {
    resetHandlers();
  });
  beforeAll(() => {
    server.listen();
  });
  afterAll(() => {
    server.close();
  });

  it("Request with query", async () => {
    const response = await request({
      url: BASE_URL,
      params: {
        a: 1,
        b: 2,
      },
    });
    expect(response.data.params).toEqual({
      a: 1,
      b: 2,
    });

    // Simple method
    const responseFormSimpleMethod = await request.get(BASE_URL, {
      params: {
        a: 1,
        b: 2,
      },
    });
    expect(responseFormSimpleMethod.data.params).toEqual({
      a: 1,
      b: 2,
    });
  });

  it("Request with param", async () => {
    const response = await request({
      url: BASE_URL + "/users/1",
    });
    expect(response.data).toEqual({
      id: 1,
      name: "Alice",
      email: "alice@example.com",
    });
    try {
      await request({
        url: BASE_URL + "/users/3",
      });
    } catch (error) {
      expect(error.response.status).toBe(404);
      expect(error.data).toEqual("");
    }

    // Simple method
    const responseFormSimpleMethod = await request.get(BASE_URL + "/users/1");
    expect(responseFormSimpleMethod.data).toEqual({
      id: 1,
      name: "Alice",
      email: "alice@example.com",
    });
    try {
      await request.get(BASE_URL + "/users/3");
    } catch (error) {
      expect(error.response.status).toBe(404);
      expect(error.data).toEqual("");
    }
  });

  it("Request with body", async () => {
    const response = await request({
      url: BASE_URL + "/users",
      method: "post",
      data: {
        name: "Bob",
        email: "bob@example.com",
      },
    });
    expect(response.data).toEqual({
      id: 3,
      name: "Bob",
      email: "bob@example.com",
    });
    expect(response.response.status).toBe(201);

    // Simple method
    const responseFormSimpleMethod = await request.post(BASE_URL + "/users", {
      data: {
        name: "Bob",
        email: "bob@example.com",
      },
    });
    expect(responseFormSimpleMethod.data).toEqual({
      id: 4,
      name: "Bob",
      email: "bob@example.com",
    });
    expect(responseFormSimpleMethod.response.status).toBe(201);
  });

  it("Request with body and param", async () => {
    const response = await request({
      url: BASE_URL + "/users/1",
      method: "put",
      data: {
        name: "Alice",
        email: "alice@example.com",
      },
    });
    expect(response.response.status).toBe(200);
    expect(response.data).toEqual({
      id: 1,
      name: "Alice",
      email: "alice@example.com",
    });

    // Simple method
    const responseFormSimpleMethod = await request.put(BASE_URL + "/users/1", {
      data: {
        name: "Alice",
        email: "alice@example.com",
      },
    });
    expect(responseFormSimpleMethod.response.status).toBe(200);
    expect(responseFormSimpleMethod.data).toEqual({
      id: 1,
      name: "Alice",
      email: "alice@example.com",
    });
  });

  it("The delete method request with param", async () => {
    await request({
      url: BASE_URL + "/users/1",
      method: "delete",
    });

    const response = await request.get(BASE_URL + "/users");
    expect(response.data).toEqual([
      { id: 2, name: "Bob", email: "bob@example.com" },
    ]);

    // Simple method
    try {
      await request.delete(BASE_URL + "/users/1");
    } catch (error) {
      expect(error.response.status).toBe(404);
      const responseFormSimpleMethod = await request.get(BASE_URL + "/users");
      expect(responseFormSimpleMethod.data).toEqual([
        { id: 2, name: "Bob", email: "bob@example.com" },
      ]);
    }
  });
  it("Request with form data", async () => {
    const data = {
      name: "Alice",
      email: "alice@example.com",
    };
    const response = await request.post(BASE_URL + "/form", {
      data,
      requestType: "form",
    });

    expect(response.data).toEqual(data);
  });

  // it("Request with file", async () => {
  //   const formData = new FormData();
  //   formData.append("type", "nisse");
  //   const file = new File(["gyldig xml"], "filnavn.xml");
  //   formData.append("file", file);

  //   const response = await request.post(BASE_URL + "/file", {
  //     data: formData,
  //   });
  //   expect(response.status).toBe(200);
  //   expect(response.statusText).toBe("OK");
  //   expect(await response.json()).toEqual({
  //     firstName: "John",
  //     lastName: "Maverick",
  //   });
  // });
});
