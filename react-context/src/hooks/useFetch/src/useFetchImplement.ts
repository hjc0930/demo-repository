import Fetch from "./Fetch";
import type { Options, Plugins, Result, Service } from "./types";
import {
  useCreation,
  useMemoizedFn,
  useMount,
  useUnmount,
  useUpdate,
} from "ahooks";
import useLatest from "@/hooks/useLatest";

const useFetchImplement = <TData, TParams extends any[]>(
  service: Service<TData, TParams>,
  options: Options<TData, TParams> = {},
  plugins: Plugins<TData, TParams>[] = []
) => {
  const serviceRef = useLatest(service);
  const update = useUpdate();
  const { manual = false, ...rest } = options;

  const fetchOptions = {
    manual,
    ...rest,
  };

  const fetchInstance = useCreation(() => {
    const initState = plugins
      .map((p) => p.onInit?.(fetchOptions))
      .filter(Boolean);

    return new Fetch<TData, TParams>(
      serviceRef,
      fetchOptions,
      update,
      Object.assign({}, ...initState)
    );
  }, []);

  fetchInstance.options = fetchOptions;
  // Run all plugins hooks
  fetchInstance.pluginImpls = plugins.map((plugin) =>
    plugin(fetchInstance, fetchOptions)
  );

  useMount(() => {
    if (!manual) {
      const params = fetchInstance.state.params || options.defaultParams || [];
      // @ts-ignore
      fetchInstance.run(...params);
    }
  });

  useUnmount(() => {
    fetchInstance.cancel();
  });

  return {
    loading: fetchInstance.state.loading,
    data: fetchInstance.state.data,
    error: fetchInstance.state.error,
    params: fetchInstance.state.params || [],
    cancel: useMemoizedFn(fetchInstance.cancel.bind(fetchInstance)),
    refresh: useMemoizedFn(fetchInstance.refresh.bind(fetchInstance)),
    refreshAsync: useMemoizedFn(fetchInstance.refreshAsync.bind(fetchInstance)),
    run: useMemoizedFn(fetchInstance.run.bind(fetchInstance)),
    runAsync: useMemoizedFn(fetchInstance.runAsync.bind(fetchInstance)),
    mutate: useMemoizedFn(fetchInstance.mutate.bind(fetchInstance)),
  } as Result<TData, TParams>;
};

export default useFetchImplement;
