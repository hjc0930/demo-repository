import { getEnvironmentVariable, getEnvironment } from "../src/services/demo";

describe("aa", () => {
  beforeAll(() => {
    process.env.PREFIX = "/aaa";
  });
  test("getEnvironment", async () => {
    const result = getEnvironment();
    expect(result).toBe("/aaa");
  });

  test("getEnvironmentVariable", () => {
    const resultVariable = getEnvironmentVariable();
    expect(resultVariable).toBe("/aaa/a/b");
  });
});
