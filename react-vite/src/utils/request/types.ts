export type Middlewares = (context: Context, next: () => Promise<void>) => void;

export interface FetchRequest extends Request {
  timer: number;
  abortController: AbortController;
}

export interface FetchResponse<T = unknown> extends Response {
  data: T
}

export interface Context{
  options?: any;
  requestInstance: Request | null;
  responseInstance: Response | null;
}

export interface Options {
  /** Url prefix */
  prefix?: string;
  /** Url suffix */
  suffix?: string;
  /** Interface timeout `default: 60s` */
  timeout?: number;
  /** Request Interceptors */
  requestInterceptors?: ((request: FetchRequest) => Promise<FetchRequest> | FetchRequest)[];
  /** Response Interceptors */
  responseInterceptors?: ((context: Context) => Promise<Response> | Response)[];
}