// 返回一个组合了所有插件的“插件”

import type { Context, Middlewares } from "../types";

const compose = (middlewares: Middlewares[]) => {
  if (!Array.isArray(middlewares)) {
    throw new TypeError("Middlewares must be an array!");
  }

  for (let i = 0; i < middlewares.length; i++) {
    if (typeof middlewares[i] !== "function") {
      throw new TypeError("Middleware must be componsed of function");
    }
  }

  return (context: Context) => {
    let index = -1;

    const dispatch = (i: number): Promise<any> => {
      if (i <= index) {
        return Promise.reject(
          new Error(
            "next() should not be called multiple times in one middleware!"
          )
        );
      }
      index = i;

      const middleware = middlewares[i];
      if (!middleware) return Promise.resolve();

      try {
        return Promise.resolve(middleware(context, () => dispatch(i + 1)));
      } catch (err) {
        return Promise.reject(err);
      }
    };

    return dispatch(0);
  };
};

export default compose;
