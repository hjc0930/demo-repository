import { Options } from "../types";
import { parse, stringify } from "qs";
import type QueryString from "qs";
import { isArray, isURLSearchParams } from "./typeAsserts";

export class RequestError {
  [x: string]: any;
  constructor(
    public name: string,
    public message: string,
    options?: Partial<Options>
  ) {
    this.stack = new Error().stack;
    Object.keys(options ?? {}).forEach((key) => {
      this[key] = options?.[key];
    });
  }
}

/**
 * 安全的JSON.parse
 */
export const safeJsonParse = (data) => {
  try {
    return JSON.parse(data);
  } catch (e) {
    // if (throwErrIfParseFail) {
    //   throw new ResponseError(
    //     response,
    //     "JSON.parse fail",
    //     data,
    //     request,
    //     "ParseError"
    //   );
    // }
  }
  return data;
};

export const timeoutToThrow = (
  millisecond: number,
  context: Options
): Promise<any> => {
  return new Promise((_, reject) => {
    setTimeout(() => {
      reject(
        new RequestError("Timeout", `timeout of ${millisecond}ms exceeded`, {
          config: context,
        })
      );
    }, millisecond);
  });
};

export function getParamObject(val) {
  if (isURLSearchParams(val)) {
    return parse(val.toString(), { strictNullHandling: true });
  }
  if (typeof val === "string") {
    return [val];
  }
  return val;
}

export const setHeaders = (context: Options, key: string, value: string) => {
  if (!context?.headers || context.headers?.has(key)) {
    return;
  }
  context.headers?.set(key, value);
};

export const mergeConfig = (
  initialOptions: Options,
  options: Options
): Options => {
  const headers = new Headers();
  if (initialOptions.headers) {
    initialOptions?.headers?.forEach((value, key) => {
      headers.set(key, value);
    });
  }
  if (options.headers) {
    options?.headers?.forEach((value, key) => {
      headers.set(key, value);
    });
  }

  return {
    ...initialOptions,
    ...options,
    headers,
    params: {
      ...getParamObject(initialOptions.params),
      ...getParamObject(options.params),
    },
    method: (options.method || initialOptions.method || "get").toLowerCase(),
  };
};

export const reqStringify = (
  val: any,
  options: QueryString.IStringifyOptions<QueryString.BooleanOptional> = {
    arrayFormat: "repeat",
    strictNullHandling: true,
  }
) => {
  return stringify(val, options);
};

export const forEach2ObjArr = (target, callback) => {
  if (!target) return;

  if (typeof target !== "object") {
    target = [target];
  }

  if (isArray(target)) {
    for (let i = 0; i < target.length; i++) {
      callback.call(null, target[i], i, target);
    }
  } else {
    for (let key in target) {
      if (Object.prototype.hasOwnProperty.call(target, key)) {
        callback.call(null, target[key], key, target);
      }
    }
  }
};
