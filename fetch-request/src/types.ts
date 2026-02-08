import Interceptor from "./core/Interceptor";
import { RequestError } from "./utils";

export type RequestType = "json" | "form";
export type ResponseType =
  | "json"
  | "text"
  | "blob"
  | "arrayBuffer"
  | "formData";

export type Method =
  | "get"
  | "post"
  | "delete"
  | "put"
  | "patch"
  | "head"
  | "options"
  | (string & {});

export interface Options extends RequestInit {
  baseURL?: string;
  url?: string;
  data?: any;
  params?: any;
  method?: Method;
  timeout?: number;
  headers?: Headers;
  requestType?: RequestType;
  responseType?: ResponseType;
  [key: string]: any;
}

export interface Context<T = any> {
  data: T;
  config: Options;
  request: Request;
  response: Response;
}

export type MiddlewareType = (
  context: any,
  next: () => Promise<void>
) => Promise<void>;

export interface RequestMethodType {
  <RequestReturn = any>(options?: Options): Promise<RequestReturn>;
  interceptors: {
    request: Interceptor<Options, RequestError>;
    response: Interceptor<Options, RequestError>;
  };
  get: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
  post: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
  put: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
  delete: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
  patch: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
  head: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
  options: <RequestReturn = any>(
    url: string,
    options?: Options
  ) => Promise<RequestReturn>;
}
