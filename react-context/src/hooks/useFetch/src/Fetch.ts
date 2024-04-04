import type { MutableRefObject } from "react";
import type {
  FetchState,
  Options,
  PluginReturn,
  Service,
  Subscribe,
} from "./types";
import { isFunction } from "./utils";

class Fetch<TData, TParams extends any[]> {
  private count = 0;
  public state: FetchState<TData, TParams> = {
    loading: false,
    params: undefined,
    data: undefined,
    error: undefined,
  };

  public pluginImpls: PluginReturn<TData, TParams>[] = [];

  constructor(
    public serviceRef: MutableRefObject<Service<TData, TParams>>,
    public options: Options<TData, TParams>,
    public subscribe: Subscribe,
    public initState: Partial<FetchState<TData, TParams>> = {}
  ) {
    this.state = {
      ...this.state,
      loading: !options.manual,
      ...initState,
    };
  }

  private setState(s: Partial<FetchState<TData, TParams>> = {}) {
    this.state = {
      ...this.state,
      ...s,
    };
    this.subscribe();
  }

  private runPluginhandler(
    event: keyof PluginReturn<TData, TParams>,
    ...rest: any[]
  ) {
    const result = this.pluginImpls
      // @ts-ignore
      .map((i) => i[event]?.(...rest))
      .filter(Boolean);
    return Object.assign({}, ...result);
  }

  public async runAsync(...params: TParams): Promise<TData> {
    this.count += 1;
    const currentCount = this.count;

    const {
      stopNow = false,
      returnNow = false,
      ...state
    } = this.runPluginhandler("onBefore", params);

    // stop request
    if (stopNow) {
      return new Promise(() => {});
    }

    this.setState({
      loading: true,
      params,
      ...state,
    });

    if (returnNow) {
      return Promise.resolve(state.data);
    }
    this.options.onBefore?.(params);

    try {
      let { servicePromise } = this.runPluginhandler(
        "onRequest",
        this.serviceRef.current,
        params
      );

      if (!servicePromise) {
        servicePromise = this.serviceRef.current(...params);
      }

      const response = await servicePromise;

      // prevent run.then when request is caceled
      if (currentCount !== this.count) {
        return new Promise(() => {});
      }

      this.setState({
        data: response,
        error: undefined,
        loading: false,
      });

      this.options.onSuccess?.(response, params);
      this.options.onFinally?.(params, response, undefined);

      if (currentCount === this.count) {
        this.runPluginhandler("onFinally", params, response, undefined);
      }
      return response;
    } catch (error: any) {
      if (currentCount !== this.count) {
        // prevent run.then when request is caceled
        return new Promise(() => {});
      }

      this.setState({
        error,
        loading: false,
      });

      this.options.onError?.(error, params);
      this.runPluginhandler("onError", error, params);
      this.options.onFinally?.(params, undefined, error);

      if (currentCount === this.count) {
        this.runPluginhandler("onFinally", params, undefined, error);
      }

      throw error;
    }
  }

  public run(...params: TParams) {
    this.runAsync(...params).catch((error) => {
      if (!this.options.onError) {
        console.error(error);
      }
    });
  }

  public cancel() {
    this.count += 1;
    this.setState({
      loading: false,
    });

    this.runPluginhandler("onCancel");
  }

  public refresh() {
    // @ts-ignore
    this.run(...Fetch(this.state.params || []));
  }

  public refreshAsync() {
    return this.runAsync(...(this.state.params || ([] as any)));
  }

  public mutate(data?: TData | ((oldData?: TData) => TData | undefined)) {
    const targetData = isFunction(data) ? data(this.state.data) : data;

    this.runPluginhandler("onMutate", targetData);

    this.setState({
      data: targetData,
    });
  }
}

export default Fetch;
