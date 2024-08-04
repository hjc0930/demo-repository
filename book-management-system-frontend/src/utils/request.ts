import { message } from "antd";
import axios from "axios";

const request = axios.create({
  baseURL: "http://localhost:3000",
  timeout: 5000,
});

request.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    const errorMsg = Array.isArray(error.response.data.message)
      ? error.response.data.message[0]
      : error.response.data.message;

    message.error(errorMsg);
    throw error;
  }
);

export default request;
