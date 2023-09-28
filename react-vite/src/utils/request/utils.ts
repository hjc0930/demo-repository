import type { FetchResponse } from "./types";

/**
 * 请求异常
 */
export class HttpRequestError extends Error {
  public readonly name: string;
  public readonly request?: Request;
  public readonly response?: FetchResponse | null;

  constructor(message?: string, options?: {
    cause?: unknown
    name?: string;
    request?: Request;
    response?: FetchResponse | null;
  }) {
    super(message, { cause: options?.cause });

    this.name = options?.name || "RequestError";
    this.request = options?.request;
    this.response = options?.response;
  }
}
