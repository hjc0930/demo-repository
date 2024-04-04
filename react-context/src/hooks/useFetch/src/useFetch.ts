import useFetchImplement from "./useFetchImplement";
import type { Service, Options, Plugins } from "./types";

const useFetch = <TData, TParams extends any[]>(
  service: Service<TData, TParams>,
  options: Options<TData, TParams> = {},
  plugins: Plugins<TData, TParams>[] = []
) => {
  return useFetchImplement<TData, TParams>(service, options, [...plugins]);
};

export default useFetch;
