// ============================================================
// 权限 Code 常量
// ============================================================

/** 菜单权限 */
export const PERMISSIONS = {
  /** 仪表盘查看 */
  DASHBOARD_VIEW: "dashboard:view",
  /** 用户列表 */
  USER_LIST: "user:list",
  /** 用户创建 */
  USER_CREATE: "user:create",
  /** 订单列表 */
  ORDER_LIST: "order:list",
} as const;

/** 按钮权限 */
export const BUTTON_PERMISSIONS = {
  /** 用户删除 */
  USER_DELETE: "user:delete",
  /** 用户编辑 */
  USER_EDIT: "user:edit",
  /** 订单导出 */
  ORDER_EXPORT: "order:export",
  /** 订单审批 */
  ORDER_APPROVE: "order:approve",
} as const;

/** 所有权限 code */
export const ALL_PERMISSIONS = {
  ...PERMISSIONS,
  ...BUTTON_PERMISSIONS,
} as const;

/** 权限 code 类型 */
export type PermissionCode = (typeof ALL_PERMISSIONS)[keyof typeof ALL_PERMISSIONS];

/** 角色 Code */
export const ROLES = {
  ADMIN: "admin",
  EDITOR: "editor",
  VIEWER: "viewer",
} as const;

/** 角色 code 类型 */
export type RoleCode = (typeof ROLES)[keyof typeof ROLES];
