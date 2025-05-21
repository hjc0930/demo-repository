import { getEnvironment, getEnvironmentVariable } from "./a";

describe("Demo", () => {
  beforeAll(() => {
    process.env.PREFIX = "/aaa";
  });

  test("getEnvironment", async () => {
    const result = getEnvironment();
    expect(result).toBe("/aaa");
  });
});
