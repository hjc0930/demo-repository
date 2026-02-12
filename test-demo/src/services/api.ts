import axios, { AxiosError } from 'axios'
import type { ApiResponse, PaginationResponse, PaginationParams } from '../types'

const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error: AxiosError<ApiResponse<unknown>>) => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，清除 token
          localStorage.removeItem('token')
          break
        case 403:
          console.error('没有权限访问')
          break
        case 404:
          console.error('请求的资源不存在')
          break
        case 500:
          console.error('服务器错误')
          break
      }
    }
    return Promise.reject(error)
  }
)

export default apiClient

// 通用请求方法
export const request = {
  get: <T>(url: string, params?: object) => apiClient.get<any, ApiResponse<T>>(url, { params }),
  post: <T>(url: string, data?: object) => apiClient.post<any, ApiResponse<T>>(url, data),
  put: <T>(url: string, data?: object) => apiClient.put<any, ApiResponse<T>>(url, data),
  delete: <T>(url: string) => apiClient.delete<any, ApiResponse<T>>(url),
}

// 分页请求
export const requestWithPagination = {
  get: <T>(url: string, params: PaginationParams) =>
    apiClient.get<any, PaginationResponse<T>>(url, { params }),
}
