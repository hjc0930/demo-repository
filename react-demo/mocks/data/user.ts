import { faker } from "@faker-js/faker/locale/en";
import type { Permission, Role, User } from "@/types/auth";

// ============================================================
// Permissions
// ============================================================

const allPermissions: Permission[] = [
  { code: "dashboard:view" },
  { code: "user:list" },
  { code: "user:create" },
  { code: "user:delete" },
  { code: "user:edit" },
  { code: "order:list" },
  { code: "order:export" },
  { code: "order:approve" },
];

const viewerPermissions: Permission[] = allPermissions.filter((p) =>
  ["dashboard:view", "user:list", "order:list"].includes(p.code),
);

const editorPermissions: Permission[] = allPermissions.filter((p) =>
  [
    "dashboard:view",
    "user:list",
    "user:create",
    "user:edit",
    "order:list",
    "order:export",
  ].includes(p.code),
);

// ============================================================
// Roles
// ============================================================

const adminRole: Role = {
  code: "admin",
  name: "Administrator",
  permissions: allPermissions,
};

const editorRole: Role = {
  code: "editor",
  name: "Editor",
  permissions: editorPermissions,
};

const viewerRole: Role = {
  code: "viewer",
  name: "Viewer",
  permissions: viewerPermissions,
};

// ============================================================
// Preset users
// ============================================================

export interface MockCredential {
  username: string;
  password: string;
  user: User;
}

export const mockUsers: MockCredential[] = [
  {
    username: "admin",
    password: "admin123",
    user: {
      id: faker.string.uuid(),
      username: "admin",
      nickname: "Administrator",
      avatar: faker.image.avatar(),
      roles: [adminRole],
    },
  },
  {
    username: "editor",
    password: "editor123",
    user: {
      id: faker.string.uuid(),
      username: "editor",
      nickname: "Editor",
      avatar: faker.image.avatar(),
      roles: [editorRole],
    },
  },
  {
    username: "viewer",
    password: "viewer123",
    user: {
      id: faker.string.uuid(),
      username: "viewer",
      nickname: "Viewer",
      avatar: faker.image.avatar(),
      roles: [viewerRole],
    },
  },
];

// ============================================================
// Token storage map
// ============================================================

const tokenUserMap = new Map<string, User>();

/** Find user by token */
export function findUserByToken(token: string): User | undefined {
  return tokenUserMap.get(token);
}

/** Authenticate user by username and password, record token on success */
export function authenticateUser(
  username: string,
  password: string,
): { token: string; user: User } | null {
  const credential = mockUsers.find(
    (u) => u.username === username && u.password === password,
  );

  if (!credential) {
    return null;
  }

  const token = `mock-token-${faker.string.uuid()}`;
  tokenUserMap.set(token, credential.user);

  return { token, user: credential.user };
}
