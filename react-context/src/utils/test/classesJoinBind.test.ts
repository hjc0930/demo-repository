import { describe, expect, test } from "vitest";
import classesJoin from "../classesJoin";

const cssModulesMock = {
  a: "#a",
  b: "#b",
  c: "#c",
  d: "#d",
  e: "#e",
  f: "#f",
} as const;

const classNamesBound = classesJoin.bind(cssModulesMock);

describe("Using classesJoin in CSS Module scenarios", () => {
  test("keeps object keys with truthy values", () => {
    expect(
      classNamesBound({
        a: true,
        b: false,
        c: 0,
        d: null,
        e: undefined,
        f: 1,
      })
    ).toBe("#a #f");
  });
  test("keeps class names undefined in bound hash", () => {
    expect(
      classNamesBound({
        a: true,
        b: false,
        c: 0,
        d: null,
        e: undefined,
        f: 1,
        x: true,
        y: null,
        z: 1,
      })
    ).toBe("#a #f x z");
  });
  test("joins arrays of class names and ignore falsy values", () => {
    expect(classNamesBound("a", 0, null, undefined, true, 1, "b")).toBe(
      "#a 1 #b"
    );
  });

  test("supports heterogenous arguments", () => {
    expect(classNamesBound({ a: true }, "b", 0)).toBe("#a #b");
  });

  test("should be trimmed", () => {
    expect(classNamesBound("", "b", {}, "")).toBe("#b");
  });

  test("returns an empty string for an empty configuration", () => {
    expect(classNamesBound({})).toBe("");
  });

  test("supports an array of class names", () => {
    expect(classNamesBound(["a", "b"])).toBe("#a #b");
  });

  test("joins array arguments with string arguments", () => {
    expect(classNamesBound(["a", "b"], "c")).toBe("#a #b #c");
    expect(classNamesBound("c", ["a", "b"])).toBe("#c #a #b");
  });

  test("handles multiple array arguments", () => {
    expect(classNamesBound(["a", "b"], ["c", "d"])).toBe("#a #b #c #d");
  });

  test("handles arrays that include falsy and true values", () => {
    expect(classNamesBound(["a", 0, null, undefined, false, true, "b"])).toBe(
      "#a #b"
    );
  });

  test("handles arrays that include arrays", () => {
    expect(classNamesBound(["a", ["b", "c"]])).toBe("#a #b #c");
  });

  test("handles arrays that include objects", () => {
    expect(classNamesBound(["a", { b: true, c: false }])).toBe("#a #b");
  });

  test("handles deep array recursion", () => {
    expect(classNamesBound(["a", ["b", ["c", { d: true }]]])).toBe(
      "#a #b #c #d"
    );
  });

  test("handles own toString() method defined on object", () => {
    expect(
      classNamesBound({
        toString: () => {
          return "classFromMethod";
        },
      })
    ).toBe("classFromMethod");
  });

  test("handles toString() method defined inherited in object", () => {
    class Class1 {}
    class Class2 extends Class1 {}
    Class1.prototype.toString = () => {
      return "classFromMethod";
    };

    expect(classNamesBound(new Class2()), "classFromMethod");
  });
});
