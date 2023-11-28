import React, {
  PropsWithChildren,
  useContext,
  useSyncExternalStore,
} from "react";
import { CreateStore } from "./context";

const storeInstance = new CreateStore({
  id: 1,
  name: "Jiacheng Huang",
});

export const StoreContext = React.createContext(storeInstance);

export const Provider = ({ children }: PropsWithChildren) => {
  return (
    <StoreContext.Provider value={storeInstance}>
      {children}
    </StoreContext.Provider>
  );
};

export function useStore(selector?: any) {
  const storeInstanceContext = useContext(StoreContext);

  const getSnapshot = () => {
    const store = selector
      ? selector(storeInstanceContext.getState())
      : storeInstanceContext.getState();
    return store;
  };

  const syncStore = useSyncExternalStore(
    storeInstanceContext.subscribe,
    getSnapshot
  );

  const set = (store?: any) => {
    storeInstanceContext.state = {
      ...storeInstanceContext.state,
      ...store,
    };
    storeInstanceContext.emitChange(store);
  };

  return {
    syncStore,
    set,
  };
}

// function createImpl(createState) {}

function create<T = any>(initState: T) {
  // return initState ? createImpl(initState) : createImpl;
}
