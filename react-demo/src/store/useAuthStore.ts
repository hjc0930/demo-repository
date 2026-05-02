import { createStore, type StoreActionMap } from "@tanstack/react-store";
import type { UserInfo } from "@/types/auth";
import { login as loginApi, getUserInfo as getUserInfoApi } from "@/services/auth";

// ============================================================
// 类型定义
// ============================================================

interface AuthState {
  token: string | null;
  user: UserInfo | null;
  loading: boolean;
}

interface AuthActions extends StoreActionMap {
  login: (username: string, password: string) => Promise<void>;
  fetchUserInfo: () => Promise<void>;
  logout: () => void;
}

// ============================================================
// Store
// ============================================================

const authStore = createStore<AuthState, AuthActions>(
  {
    token: localStorage.getItem("token"),
    user: null,
    loading: false,
  },
  ({ setState, get }) => ({
    login: async (username: string, password: string) => {
      setState((s) => ({ ...s, loading: true }));
      try {
        const { token, user: rawUser } = await loginApi({ username, password });
        localStorage.setItem("token", token);

        // 平铺 roles 和 permissions
        const roles = rawUser.roles.map((r) => r.code);
        const permissions = [
          ...new Set(
            rawUser.roles.flatMap((r) => r.permissions.map((p) => p.code)),
          ),
        ];
        const user: UserInfo = {
          id: rawUser.id,
          username: rawUser.username,
          nickname: rawUser.nickname,
          avatar: rawUser.avatar,
          roles,
          permissions,
        };

        setState((s) => ({ ...s, token, user, loading: false }));
      } catch {
        setState((s) => ({ ...s, loading: false }));
        throw new Error("登录失败");
      }
    },

    fetchUserInfo: async () => {
      const { token } = get();
      if (!token) return;

      setState((s) => ({ ...s, loading: true }));
      try {
        const { user } = await getUserInfoApi();
        setState((s) => ({ ...s, user, loading: false }));
      } catch {
        setState((s) => ({
          ...s,
          loading: false,
          token: null,
          user: null,
        }));
        localStorage.removeItem("token");
      }
    },

    logout: () => {
      localStorage.removeItem("token");
      setState((s) => ({ ...s, token: null, user: null, loading: false }));
    },
  }),
);

export default authStore;
