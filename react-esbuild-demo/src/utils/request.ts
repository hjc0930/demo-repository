import axios from "axios";

const request = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL,
  timeout: 60000,
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // TODO: 在发送请求之前做些什么
    return config;
  },
  (err) => {
    console.error(err);
  }
);

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    // 2xx 范围内的http状态码都会触发该函数。
    // 对响应数据做点什么
    return response.data;
  },
  (err) => {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    console.error(err);
    throw err;
  }
);

export { request };
