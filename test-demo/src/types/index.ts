export interface User {
  id: number
  name: string
  email: string
  role: 'admin' | 'user'
  createdAt: string
}

export interface Todo {
  id: number
  title: string
  completed: boolean
  userId: number
}

export interface ApiResponse<T> {
  data: T
  message: string
  code: number
}

export interface PaginationParams {
  page: number
  pageSize: number
}

export interface PaginationResponse<T> {
  data: T[]
  total: number
  page: number
  pageSize: number
}
