/**
 * @type {import("jest").Config}
 */
const config = {
  testEnvironment: "node",
  transform: {},
  transformIgnorePatterns: [],

  // 测试文件匹配规则
  testMatch: ["**/test/**/*.[jt]s?(x)", "**/?(*.)+(spec|test).[tj]s?(x)"],
};

export default config;
