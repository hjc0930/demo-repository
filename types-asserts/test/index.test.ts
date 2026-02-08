import { describe, test, expect } from "vitest";
import {
  isArray,
  isBoolean,
  isDate,
  isFunction,
  isNil,
  isNumber,
  isObject,
  isString,
  isUndef,
  isURLSearchParams,
} from "../src";

describe("Type Check", () => {
  test("isObject", () => {
    expect(isObject({})).toBe(true);
    expect(isObject([])).toBe(false);
    expect(isObject(null)).toBe(false);
    expect(isObject(undefined)).toBe(false);
    expect(isObject("string")).toBe(false);
    expect(isObject(123)).toBe(false);
    expect(isObject(false)).toBe(false);
  });

  test("isFunction", () => {
    expect(isFunction(() => {})).toBe(true);
    expect(isFunction({})).toBe(false);
    expect(isFunction([])).toBe(false);
    expect(isFunction(null)).toBe(false);
    expect(isFunction(undefined)).toBe(false);
    expect(isFunction("string")).toBe(false);
    expect(isFunction(123)).toBe(false);
    expect(isFunction(false)).toBe(false);
  });

  test("isArray", () => {
    expect(isArray([])).toBe(true);
    expect(isArray({})).toBe(false);
    expect(isArray(null)).toBe(false);
    expect(isArray(undefined)).toBe(false);
    expect(isArray("string")).toBe(false);
    expect(isArray(123)).toBe(false);
  });

  test("isURLSearchParams", () => {
    expect(isURLSearchParams(new URLSearchParams())).toBe(true);
    expect(isURLSearchParams({})).toBe(false);
    expect(isURLSearchParams([])).toBe(false);
  });

  test("isDate", () => {
    expect(isDate(new Date())).toBe(true);
    expect(isDate({})).toBe(false);
    expect(isDate([])).toBe(false);
    expect(isDate(null)).toBe(false);
  });

  test("isString", () => {
    expect(isString("string")).toBe(true);
    expect(isString({})).toBe(false);
    expect(isString([])).toBe(false);
  });

  test("isNumber", () => {
    expect(isNumber(123)).toBe(true);
    expect(isNumber({})).toBe(false);
    expect(isNumber([])).toBe(false);
  });

  test("isBoolean", () => {
    expect(isBoolean(true)).toBe(true);
    expect(isBoolean(false)).toBe(true);
    expect(isBoolean({})).toBe(false);
  });

  test("isNil", () => {
    expect(isNil(null)).toBe(true);
    expect(isNil(undefined)).toBe(true);
    expect(isNil({})).toBe(false);
    expect(isNil([])).toBe(false);
    expect(isNil("string")).toBe(false);
    expect(isNil(123)).toBe(false);
  });

  test("isUndef", () => {
    expect(isUndef(undefined)).toBe(true);
    expect(isUndef({})).toBe(false);
    expect(isUndef([])).toBe(false);
    expect(isUndef("string")).toBe(false);
    expect(isUndef(123)).toBe(false);
    expect(isUndef(false)).toBe(false);
  });
});
