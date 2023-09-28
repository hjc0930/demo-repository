import { useContext, useEffect, useState } from "react";
import { AppContext, QueryState } from "../App";

const useAppStore = (
  selector?: (state: QueryState) => any
): [QueryState, (p: Partial<QueryState>) => void] => {
  const storeCtx = useContext(AppContext)!;

  const defaultSelectState = selector
    ? selector?.(storeCtx.getState())
    : storeCtx.getState();

  const [state, setState] = useState(defaultSelectState);

  // add subscribe

  useEffect(() => {
    const unsubscribe = storeCtx.subscribe((s: QueryState) => {
      const selectState = selector ? selector(s) : s;
      setState(selectState);
    });

    return () => unsubscribe();
  }, []);

  return [state, storeCtx.setState];
};

export default useAppStore;
