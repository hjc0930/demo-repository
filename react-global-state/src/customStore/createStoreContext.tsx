import {
  PropsWithChildren,
  createContext,
  useContext,
  useRef,
  useSyncExternalStore,
} from "react";
import { Store, createStore } from "./createStore";

function createStoreContext<T>(initState: T) {
  const StoreContext = createContext<Store<T> | null>(null);

  const StoreProvider = ({ children }: PropsWithChildren) => {
    const storeRef = useRef<Store<T>>();

    if (!storeRef.current) {
      storeRef.current = createStore(initState);
    }

    return (
      <StoreContext.Provider value={storeRef.current}>
        {children}
      </StoreContext.Provider>
    );
  };

  function useStore(): [T, Store<T>["set"]];
  function useStore<R extends keyof T>(
    selector: (state: T) => T[R]
  ): [T[R], Store<T>["set"]];
  function useStore<R extends keyof T>(
    selector?: (state: T) => T[R]
  ): [T[R] | T, Store<T>["set"]] {
    const stoteCtx = useContext(StoreContext)!;

    const state = useSyncExternalStore(stoteCtx.subscribe, () => {
      return selector ? selector(stoteCtx.get()) : stoteCtx.get();
    });

    return [state, stoteCtx.set];
  }

  return {
    StoreProvider,
    useStore,
  };
}

export default createStoreContext;
