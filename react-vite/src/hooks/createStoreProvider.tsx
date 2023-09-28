import {
  createContext,
  PropsWithChildren,
  useContext,
  useEffect,
  useRef,
  useState,
} from "react";
import { createStore, Store } from "./createContext";

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

  const useStore = (
    selector?: (state: T) => any
  ): [Partial<T>, (p: Partial<T>) => void] => {
    const storeCtx = useContext(StoreContext)!;

    const defaultSelectState = selector
      ? selector?.(storeCtx.getState())
      : storeCtx.getState();

    const [state, setState] = useState(defaultSelectState);

    // add subscribe
    useEffect(() => {
      const unsubscribe = storeCtx.subscribe((s: T) => {
        const selectState = selector ? selector(s) : s;

        setState(selectState);
      });

      return () => unsubscribe();
    }, []);

    return [state, storeCtx.setState];
  };

  return {
    StoreProvider,
    useStore,
  };
}

export default createStoreContext;
