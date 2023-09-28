import type { Middlewares } from "../types";

const fetchMiddleware: Middlewares = async (ctx, next) => {
  const { options } = ctx;
  const request = new Request(options.url);

  const response = await fetch(request);

  ctx.responseInstance = response;
  ctx.requestInstance = request;

  await next();
}

export default fetchMiddleware;