import { describe, expect, test } from "vitest";
import classesJoin from "../classesJoin";

describe("classesJoin", () => {
  test("keeps object keys with truthy values", () => {
    expect(
      classesJoin({
        a: true,
        b: false,
        c: 0,
        d: null,
        e: undefined,
        f: 1,
      })
    ).toBe("a f");
  });

  test("joins arrays of class names and ignore falsy values", () => {
    expect(classesJoin("a", 0, null, undefined, true, 1, "b")).toBe("a 1 b");
  });

  test("supports heterogenous arguments", () => {
    expect(classesJoin({ a: true }, "b", 0)).toBe("a b");
  });

  test("should be trimmed", () => {
    expect(classesJoin("", "b", {}, "")).toBe("b");
  });

  test("returns an empty string for an empty configuration", () => {
    expect(classesJoin({})).toBe("");
  });

  test("supports an array of class names", () => {
    expect(classesJoin(["a", "b"])).toBe("a b");
  });

  test("joins array arguments with string arguments", () => {
    expect(classesJoin(["a", "b"], "c")).toBe("a b c");
    expect(classesJoin("c", ["a", "b"])).toBe("c a b");
  });

  test("handles multiple array arguments", () => {
    expect(classesJoin(["a", "b"], ["c", "d"])).toBe("a b c d");
  });

  test("handles arrays that include falsy and true values", () => {
    expect(classesJoin(["a", 0, null, undefined, false, true, "b"])).toBe(
      "a b"
    );
  });

  test("handles arrays that include arrays", () => {
    expect(classesJoin(["a", ["b", "c"]])).toBe("a b c");
  });

  test("handles arrays that include objects", () => {
    expect(classesJoin(["a", { b: true, c: false }])).toBe("a b");
  });

  test("handles deep array recursion", () => {
    expect(classesJoin(["a", ["b", ["c", { d: true }]]])).toBe("a b c d");
  });

  test("handles arrays that are empty", () => {
    expect(classesJoin("a", [])).toBe("a");
  });

  test("handles nested arrays that have empty nested arrays", () => {
    expect(classesJoin("a", [[]])).toBe("a");
  });

  test("handles all types of truthy and falsy property values as expected", () => {
    expect(
      classesJoin({
        // falsy:
        null: null,
        emptyString: "",
        noNumber: NaN,
        zero: 0,
        negativeZero: -0,
        false: false,
        undefined: undefined,

        // truthy (literally anything else):
        nonEmptyString: "foobar",
        whitespace: " ",
        function: Object.prototype.toString,
        emptyObject: {},
        nonEmptyObject: { a: 1, b: 2 },
        emptyList: [],
        nonEmptyList: [1, 2, 3],
        greaterZero: 1,
      })
    ).toBe(
      "nonEmptyString whitespace function emptyObject nonEmptyObject emptyList nonEmptyList greaterZero"
    );
  });

  test("handles toString() method defined on object", function () {
    expect(
      classesJoin({
        toString: function () {
          return "classFromMethod";
        },
      })
    ).toBe("classFromMethod");
  });

  test("handles toString() method defined inherited in object", function () {
    class Class1 {}
    class Class2 extends Class1 {}
    Class1.prototype.toString = () => {
      return "classFromMethod";
    };
    expect(classesJoin(new Class2())).toBe("classFromMethod");
  });
});
