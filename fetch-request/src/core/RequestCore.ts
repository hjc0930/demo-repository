import Interceptor, { InterceptorStack } from "./Interceptor";
import { mergeConfig, RequestError } from "../utils";
import { MiddlewareType, Options } from "../types";
import Middleware from "./Middlewares";

class RequestCore {
  private initialOptions: Options;
  private middleware: Middleware;
  public interceptors: {
    request: Interceptor<Options, RequestError>;
    response: Interceptor<Options, RequestError>;
  };

  constructor(
    initialOptions: Options = {},
    middlewares: MiddlewareType[] = []
  ) {
    this.initialOptions = initialOptions;
    this.interceptors = {
      request: new Interceptor(),
      response: new Interceptor(),
    };
    this.middleware = new Middleware();

    middlewares.forEach((middleware) => {
      this.middleware.use(middleware);
    });
  }

  public request = (options: Options = {}) => {
    const config = mergeConfig(this.initialOptions, options);

    // Reference the Axios interceptor implementation
    const requestInterceptorChain: (
      | InterceptorStack["onFullfilled"]
      | InterceptorStack["onRejected"]
    )[] = [];
    this.interceptors.request.forEach((interceptor) => {
      requestInterceptorChain.push(
        interceptor.onFullfilled,
        interceptor.onRejected
      );
    });
    const responseInterceptorChain: (
      | InterceptorStack["onFullfilled"]
      | InterceptorStack["onRejected"]
    )[] = [];
    this.interceptors.response.forEach((interceptor) => {
      responseInterceptorChain.push(
        interceptor.onFullfilled,
        interceptor.onRejected
      );
    });

    const chain = [this.dispatchRequest, undefined];
    chain.unshift(...requestInterceptorChain);
    chain.push(...responseInterceptorChain);

    const length = chain.length;
    let i = 0;
    let promise = Promise.resolve(config);

    while (i < length) {
      promise = promise.then<Options, any>(chain[i++], chain[i++]);
    }
    return promise;
  };

  private dispatchRequest = (ctx: Options) => {
    return new Promise((resolve, reject) => {
      this.middleware.execute(ctx).then(() => {
        resolve(ctx);
      }, reject);
    });
  };
}

export default RequestCore;
