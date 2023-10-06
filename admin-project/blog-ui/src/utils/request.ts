import axios from "axios";
import { ElMessage } from "element-plus";

const request = axios.create({
  baseURL: "",
  timeout: 30 * 1000,
  responseType: "json",
});

request.interceptors.response.use(
  (respose) => {
    return respose.data;
  },
  (error) => {
    ElMessage.error(error?.response?.message);
    throw error;
  }
);

export default request;
