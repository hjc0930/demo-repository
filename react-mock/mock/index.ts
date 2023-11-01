import Mock from "mockjs";
import axios from "axios";

Mock.setup({
  timeout: "200-400",
});

Mock.mock("/api/page", "get", () => ({
  code: 200,
  msg: "Successful",
  data: [1, 2, 3],
}));
