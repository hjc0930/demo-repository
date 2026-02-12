import { request } from './api'
import type { User } from '../types'

export interface LoginParams {
  username: string
  password: string
}

export interface RegisterParams {
  username: string
  password: string
  email: string
}

export interface LoginResponse {
  token: string
  user: User
}

// 用户登录
export const login = (params: LoginParams) =>
  request.post<LoginResponse>('/auth/login', params)

// 用户注册
export const register = (params: RegisterParams) =>
  request.post<LoginResponse>('/auth/register', params)

// 获取用户信息
export const getUserInfo = () =>
  request.get<User>('/user/info')

// 更新用户信息
export const updateUserInfo = (data: Partial<User>) =>
  request.put<User>('/user/info', data)

// 获取用户列表
export const getUserList = () =>
  request.get<User[]>('/user/list')
