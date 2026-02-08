import type { MiddlewareType, Options } from "../types";
import { RequestError, safeJsonParse } from "../utils";

const parseResponseMiddleware: MiddlewareType = async (ctx: Options, next) => {
  await next();
  const { response, responseType = "json" } = ctx;
  let cloneResponse: Response;
  let data = null;

  cloneResponse = response.clone();

  if (responseType === "json") {
    const text = await response.text();
    data = safeJsonParse(text);
  } else {
    try {
      data = response[responseType]();
    } catch (error) {
      throw new RequestError("ParseError", "responseType not support", {
        ...ctx,
        data: null,
        response: cloneResponse,
      });
    }
  }

  if (cloneResponse.status < 400) {
    ctx.data = data;
    ctx.response = cloneResponse;
  } else {
    throw new RequestError("HttpError", "http error", {
      ...ctx,
      data,
      response: cloneResponse,
    });
  }
};

export default parseResponseMiddleware;
