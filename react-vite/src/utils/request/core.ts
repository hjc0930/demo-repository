import type { Options, FetchRequest } from "./types";
import { HttpRequestError } from "./utils";

class HttpRequest {
  private readonly initOptions?: Options;

  constructor(initOptions: Options = {}) {
    this.initOptions = initOptions;
  }

  private async beforeHandler(url: string, options?: RequestInit): Promise<FetchRequest> {
    const { prefix = "", suffix = "", timeout = 60 * 1000 } = this.initOptions ?? {} as Options;
    const requestInterceptors = this.initOptions?.requestInterceptors ?? [];

    const abortController = new AbortController();

    const urlSplicing = prefix + url + suffix;

    const request = new Request(urlSplicing, {
      signal: abortController.signal,
      ...options,
      body: options?.body ? JSON.stringify(options?.body) : undefined
    });

    const timer = setTimeout(() => {
      abortController.abort();
    }, timeout);

    let fetchRequest = Object.assign(request, { timer, abortController });

    if (!requestInterceptors.length) {
      return fetchRequest;
    }

    for (const interceptors of requestInterceptors) {
      fetchRequest = await interceptors(fetchRequest);
    }

    return fetchRequest;
  }

  private async afterHandler(request: Request, response: Response): Promise<Response> {
    const responseInterceptors = this.initOptions?.responseInterceptors ?? [];

    if (!responseInterceptors?.length) {
      return response;
    }

    for (const interceptor of responseInterceptors) {
      response = await interceptor(
        { requestInstance: request, responseInstance: response }
      );
    }

    return response;
  }

  public async execute(url: string, options?: RequestInit): Promise<Response> {
    const request = await this.beforeHandler(url, options);

    try {
      let response = await fetch(request);

      response = await this.afterHandler(request, response)

      return response;
    } catch (error: any) {
      throw new HttpRequestError(`A ${error.name} error`, {
        request,
        cause: error
      });
    } finally {
      clearTimeout(request.timer);
    }
  }

}

export default HttpRequest;
