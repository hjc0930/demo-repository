export const isObject = (value: unknown): value is Record<any, any> => {
  return value !== null && !Array.isArray(value) && typeof value === "object";
};

export const isFunction = (value: unknown): value is (...args: any) => any => {
  return typeof value === "function";
};

export const isArray = (value: unknown): value is any[] => {
  return Array.isArray(value);
};

export const isURLSearchParams = (value: unknown): value is URLSearchParams => {
  return (
    typeof URLSearchParams !== "undefined" && value instanceof URLSearchParams
  );
};

export function isDate(value: unknown): value is Date {
  return (
    typeof value === "object" &&
    Object.prototype.toString.call(value) === "[object Date]"
  );
}

export const isString = (value: unknown): value is string => {
  return typeof value === "string";
};

export const isNumber = (value: unknown): value is number => {
  return typeof value === "number";
};

export const isBoolean = (value: unknown): value is boolean => {
  return typeof value === "boolean";
};

export const isUndef = (value: unknown): value is undefined => {
  return typeof value === "undefined";
};

export const isNil = (value: unknown): value is null | undefined => {
  return value === null || isUndef(value);
};
