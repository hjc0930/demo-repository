import type { MiddlewareType, Options } from "../types";

class Middleware {
  private middlewares: MiddlewareType[] = [];

  public use = (middleware: MiddlewareType) => {
    this.middlewares.push(middleware);
  };

  public execute = (ctx: Options) => {
    const fn = this.compose([...this.middlewares]);
    return fn(ctx);
  };

  private compose = (middlewares: MiddlewareType[]) => {
    if (!Array.isArray(middlewares)) {
      throw new TypeError("Middleware stack must be an array!");
    }
    for (const fn of middlewares) {
      if (typeof fn !== "function")
        throw new TypeError("Middleware must be composed of functions!");
    }

    return (
      ctx: Parameters<MiddlewareType>[0],
      next?: Parameters<MiddlewareType>[1]
    ) => {
      // last called middleware #
      let index = -1;
      const dispatch = (i: number) => {
        if (i <= index) {
          return Promise.reject(new Error("next() called multiple times"));
        }
        index = i;
        const fn = middlewares[i] || next;
        if (!fn) return Promise.resolve();
        try {
          return Promise.resolve(fn(ctx, dispatch.bind(null, i + 1)));
        } catch (err) {
          return Promise.reject(err);
        }
      };
      return dispatch(0);
    };
  };
}

export default Middleware;
