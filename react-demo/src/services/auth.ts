import axios from "axios";
import type {
  LoginRequest,
  LoginResponse,
  UserInfoResponse,
} from "@/types/auth";
import { snackbarStore } from "@/store/useSnackbarStore";

const apiClient = axios.create({
  baseURL: "/api",
  timeout: 10000,
});

// 请求拦截器：自动附加 token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器：统一处理错误
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/login";
      return Promise.reject(error);
    }

    const message =
      error.response?.data?.message ?? "Request failed, please try again";
    snackbarStore.state.addError(message);

    return Promise.reject(error);
  },
);

/** 登录 */
export async function login(data: LoginRequest) {
  const response = await apiClient.post<{ code: number; data: LoginResponse }>(
    "/auth/login",
    data,
  );
  return response.data.data;
}

/** 获取当前用户信息 */
export async function getUserInfo() {
  const response = await apiClient.get<{
    code: number;
    data: UserInfoResponse;
  }>("/auth/me");
  return response.data.data;
}
