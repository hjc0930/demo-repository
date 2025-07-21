// app.test.js
import app from "../index";
import { createServer } from "http";

describe("Express API 测试 (使用 Fetch API)", () => {
  let server;
  let baseUrl;

  // 启动服务器
  beforeAll((done) => {
    server = createServer(app);
    server.listen(0, () => {
      // 0 表示使用随机可用端口
      const port = server.address().port;
      baseUrl = `http://localhost:${port}`;
      done();
    });
  });

  // 关闭服务器
  afterAll((done) => {
    server.close(done);
  });

  // 测试 GET /api/users
  it("应该返回所有用户", async () => {
    const response = await fetch(`${baseUrl}/api/users`);
    const data = await response.json();

    expect(response.status).toBe(200);
    expect(data).toEqual([
      { id: 1, name: "John" },
      { id: 2, name: "Jane" },
    ]);
    expect(response.headers.get("content-type")).toMatch(/json/);
  });

  // 测试 GET /api/users/:id
  it("应该返回指定ID的用户", async () => {
    const userId = 1;
    const response = await fetch(`${baseUrl}/api/users/${userId}`);
    const data = await response.json();

    expect(response.status).toBe(200);
    expect(data).toEqual({ id: userId, name: "Test User" });
  });

  // 测试 POST /api/users
  it("应该创建新用户", async () => {
    const newUser = { name: "Alice", email: "alice@example.com" };

    const response = await fetch(`${baseUrl}/api/users`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newUser),
    });

    const data = await response.json();

    expect(response.status).toBe(201);
    expect(data).toMatchObject(newUser);
    expect(data).toHaveProperty("id");
  });
});
