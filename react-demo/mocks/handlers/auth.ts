import { http, HttpResponse, delay } from "msw";
import { authenticateUser, findUserByToken } from "../data/user";
import type { LoginRequest } from "@/types/auth";

const API_PREFIX = "/api/auth";

export const authHandlers = [
  // Login
  http.post(`${API_PREFIX}/login`, async ({ request }) => {
    await delay(300);
    const body = (await request.json()) as LoginRequest;

    if (!body.username || !body.password) {
      return HttpResponse.json(
        { code: 400, message: "Username and password are required" },
        { status: 400 },
      );
    }

    const result = authenticateUser(body.username, body.password);

    if (!result) {
      return HttpResponse.json(
        { code: 400, message: "Invalid username or password" },
        { status: 400 },
      );
    }

    return HttpResponse.json({
      code: 200,
      data: {
        token: result.token,
        user: result.user,
      },
    });
  }),

  // Get current user info
  http.get(`${API_PREFIX}/me`, async ({ request }) => {
    await delay(200);

    const authHeader = request.headers.get("Authorization");
    if (!authHeader?.startsWith("Bearer ")) {
      return HttpResponse.json(
        { code: 401, message: "No valid authentication token provided" },
        { status: 401 },
      );
    }

    const token = authHeader.replace("Bearer ", "");
    const user = findUserByToken(token);

    if (!user) {
      return HttpResponse.json(
        { code: 401, message: "Token is invalid or expired" },
        { status: 401 },
      );
    }

    // Flatten roles and permissions into code string arrays
    const roles = user.roles.map((r) => r.code);
    const permissions = [
      ...new Set(user.roles.flatMap((r) => r.permissions.map((p) => p.code))),
    ];

    return HttpResponse.json({
      code: 200,
      data: {
        user: {
          id: user.id,
          username: user.username,
          nickname: user.nickname,
          avatar: user.avatar,
          roles,
          permissions,
        },
      },
    });
  }),
];
