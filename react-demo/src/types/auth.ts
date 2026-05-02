// ============================================================
// 内部数据结构（Mock 数据使用）
// ============================================================

/** 权限项 */
export interface Permission {
  code: string;
}

/** 角色 */
export interface Role {
  code: string;
  name: string;
  permissions: Permission[];
}

/** 用户（内部结构） */
export interface User {
  id: string;
  username: string;
  nickname: string;
  avatar: string;
  roles: Role[];
}

// ============================================================
// API 请求/响应类型
// ============================================================

/** 登录请求 */
export interface LoginRequest {
  username: string;
  password: string;
}

/** 登录响应 */
export interface LoginResponse {
  token: string;
  user: User;
}

/** GET /api/auth/me 响应中的用户信息（平铺结构） */
export interface UserInfo {
  id: string;
  username: string;
  nickname: string;
  avatar: string;
  roles: string[];
  permissions: string[];
}

/** GET /api/auth/me 响应 */
export interface UserInfoResponse {
  user: UserInfo;
}
