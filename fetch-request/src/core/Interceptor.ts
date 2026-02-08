import { RequestError } from "../utils";

export interface InterceptorStack<T = any, E = any> {
  onFullfilled?: (context: T) => T;
  onRejected?: (error: E) => T;
}

class Interceptor<T = any, E = any> {
  private interceptors: InterceptorStack<T, E>[] = [];

  public use = (
    onFullfilled?: InterceptorStack<T, E>["onFullfilled"],
    onRejected?: InterceptorStack<T, E>["onRejected"]
  ): number => {
    this.interceptors.push({
      onFullfilled,
      onRejected,
    });

    return this.interceptors.length - 1;
  };

  public remove = (id: number) => {
    if (this.interceptors[id]) {
      this.interceptors[id] = {};
    }
  };

  public clear = () => {
    this.interceptors = [];
  };

  public forEach = (fn: (interceptor: InterceptorStack<T, E>) => void) => {
    this.interceptors.forEach(fn);
  };
}

export default Interceptor;
