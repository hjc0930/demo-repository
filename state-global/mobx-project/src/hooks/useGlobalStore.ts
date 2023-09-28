import { RootStoresContext, RootStoresType } from "../stores"
import { useContext } from "react"


interface UseGlobalStore {
  (): RootStoresType;
  <T extends RootStoresType, K extends keyof RootStoresType>(name: K): T[K]
}

const useGlobalStore: UseGlobalStore = <T extends RootStoresType, K extends keyof T>
  (name?: K): RootStoresType | T[K] => {
  const store = useContext(RootStoresContext) as T;

  if (!name) return store;

  return store[name];
}

export default useGlobalStore