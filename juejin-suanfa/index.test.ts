import { expect, test, describe } from "bun:test";
import solution from ".";

describe("solution", () => {
  test("n = 5 ,k = 2 ,data = [1, 2, 3, 3, 2]", () => {
    expect(solution(5, 2, [1, 2, 3, 3, 2])).toBe(9);
  });

  test("n = 6 ,k = 3 ,data = [4, 1, 5, 2, 1, 3]", () => {
    expect(solution(6, 3, [4, 1, 5, 2, 1, 3])).toBe(9);
  });

  test("n = 4 ,k = 1 ,data = [3, 2, 4, 1]", () => {
    expect(solution(4, 1, [3, 2, 4, 1])).toBe(10);
  });
});
