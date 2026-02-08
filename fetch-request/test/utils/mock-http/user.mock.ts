import { http, HttpResponse } from "msw";
import { BASE_URL } from "../constand";

interface User {
  id: number;
  name: string;
  email: string;
}

const userUrl = BASE_URL + "/users";
// 模拟的用户数据
let users: User[] = [
  { id: 1, name: "Alice", email: "alice@example.com" },
  { id: 2, name: "Bob", email: "bob@example.com" },
];
const onResetData = () => {
  users = [
    { id: 1, name: "Alice", email: "alice@example.com" },
    { id: 2, name: "Bob", email: "bob@example.com" },
  ];
};

// 获取所有用户
const getAllUsers = http.get(userUrl, () => {
  return new HttpResponse(JSON.stringify(users));
  // return HttpResponse.json(users);
});

// 获取单个用户
const getUserById = http.get(userUrl + "/:id", ({ params }) => {
  const { id } = params as { id: string };
  const user = users.find((u) => u.id === parseInt(id));
  if (user) {
    return HttpResponse.json(user);
  } else {
    return new HttpResponse(null, {
      status: 404,
    });
  }
});

// 创建新用户
const createUser = http.post(userUrl, async ({ request }) => {
  const user = (await request.json()) as User;

  const newUser = { ...user, id: users.length + 1 };
  users.push(newUser);
  return new HttpResponse(JSON.stringify(newUser), {
    status: 201,
  });
});

// 更新用户
const updateUser = http.put(userUrl + "/:id", async ({ request, params }) => {
  const user = (await request.json()) as User;
  const { id } = params;
  const userIndex = users.findIndex((u) => u.id === parseInt(id as string));

  if (userIndex !== -1) {
    users[userIndex] = { ...users[userIndex], ...user };
    return new HttpResponse(JSON.stringify(users[userIndex]), {
      status: 200,
    });
  } else {
    return new HttpResponse("User not found", {
      status: 404,
    });
  }
});

// 删除用户
const deleteUser = http.delete(userUrl + "/:id", ({ params }) => {
  const { id } = params;
  const userIndex = users.findIndex((u) => u.id === parseInt(id as string));
  if (userIndex !== -1) {
    users.splice(userIndex, 1);
    return new HttpResponse(null, {
      status: 204,
    });
  } else {
    return new HttpResponse("User not found", {
      status: 404,
    });
  }
});

// 导出所有的模拟处理器
const userHandlers = [
  getAllUsers,
  getUserById,
  createUser,
  updateUser,
  deleteUser,
];

export { onResetData };

export default userHandlers;
