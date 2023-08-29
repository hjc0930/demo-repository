import { RootStore, StoreContext } from "@/stores";
import { useContext } from "react";

interface UseRootStore {
  (): typeof RootStore | null;
  <V extends keyof typeof RootStore>(storeName: V): typeof RootStore[V];
}

// useRootStore
const useRootStore: UseRootStore = <V extends keyof typeof RootStore>(
  storeName?: V
): null | typeof RootStore | typeof RootStore[V] => {
  const store = useContext(StoreContext);

  if (!storeName || !store) return store;

  return store[storeName];
};

export { useRootStore };
