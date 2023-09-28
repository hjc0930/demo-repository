import type { Middlewares } from "../types";


const cancelMiddleware: Middlewares = async (ctx, next) => {
  const { timeout, signal } = ctx.options;
  const abortController = new AbortController();

  if (typeof timeout !== "number" || timeout <= 0 || !signal) {
    return await next();
  }

  ctx.options.signal = abortController.signal;

  signal.addEventListener("abort", () => {
    abortController.abort()
  })

  AbortSignal.timeout(timeout).addEventListener("abort", () => {
    abortController.abort();
  })
  await next();
}

export default cancelMiddleware;