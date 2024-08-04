import type { LoginParams, RegisterParams } from "../types/auth";
import request from "../utils/request";

export const login = (data: LoginParams) => {
  return request.post("/user/login", data);
};

export const register = (data: RegisterParams) => {
  return request.post("/user/register", data);
};
