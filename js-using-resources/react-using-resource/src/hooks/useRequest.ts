import { useState, useCallback, useRef, useEffect } from "react";

/**
 * 自动资源释放 Scope：管理 loading + 请求取消
 * 利用 await using 自动 dispose，函数退出/报错/卸载自动清理
 */
class AsyncRequestScope implements AsyncDisposable {
  private readonly setLoading: (v: boolean) => void;
  private readonly isMounted: React.RefObject<boolean>;
  public readonly abortController: AbortController;

  constructor(
    setLoading: (v: boolean) => void,
    isMounted: React.RefObject<boolean>,
  ) {
    this.setLoading = setLoading;
    this.isMounted = isMounted;
    this.abortController = new AbortController();

    // 组件未卸载才开启 loading
    if (this.isMounted.current) {
      this.setLoading(true);
    }
  }

  // 资源自动释放：await using 会自动调用
  async [Symbol.asyncDispose]() {
    // 取消请求
    this.abortController.abort();
    // 组件未卸载才关闭 loading
    if (this.isMounted.current) {
      this.setLoading(false);
    }
  }
}

/**
 * 基于 using 语法的请求 Hook
 * @param apiFn 请求函数，接收 signal 用于取消
 * @param immediate 是否立即执行
 */
export const useRequest = <T>(
  apiFn: (signal: AbortSignal) => Promise<T>,
  immediate = false,
) => {
  const [data, setData] = useState<T | null>(null);
  const [error, setError] = useState<Error | null>(null);
  const [loading, setLoading] = useState(false);
  const isMounted = useRef(true);

  // 组件卸载标记
  useEffect(() => {
    isMounted.current = true;
    return () => {
      isMounted.current = false;
    };
  }, []);

  // 手动触发请求，核心：await using 自动管理资源
  const runAsync = useCallback(async () => {
    // 每次请求新建 Scope，自动管理 loading + 取消
    await using scope = new AsyncRequestScope(setLoading, isMounted);

    try {
      setError(null);
      const res = await apiFn(scope.abortController.signal);
      if (isMounted.current) setData(res);
      return res;
    } catch (err) {
      if (isMounted.current) setError(err as Error);
      throw err;
    }
  }, [apiFn]);

  const run = useCallback(() => {
    runAsync();
  }, [runAsync]);

  // 立即执行
  useEffect(() => {
    if (immediate) {
      Promise.try(run);
    }
  }, [immediate, run]);

  return { data, error, loading, run, runAsync };
};
