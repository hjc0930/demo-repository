import { MiddlewareType, Options } from "../types";
import { timeoutToThrow } from "../utils";

const fetchMiddleware: MiddlewareType = async (ctx: Options, next) => {
  const { url, timeout = 0, ...restOptions } = ctx;

  // TODO: more adapter
  const adapter = fetch;

  if (!adapter) {
    throw new Error("Global fetch not exist!");
  }

  const request = new Request(url || "", restOptions);
  let response: Response;

  // timeout
  if (timeout > 0) {
    response = await Promise.race([
      adapter(request),
      timeoutToThrow(timeout, ctx),
    ]);
  } else {
    response = await adapter(request);
  }
  ctx.data = null;
  ctx.request = request;
  ctx.response = response;

  await next();
};

export default fetchMiddleware;
