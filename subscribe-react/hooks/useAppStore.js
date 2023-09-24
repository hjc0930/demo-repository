import { useContext, useSyncExternalStore } from "react"
import { AppContext } from "../src/stores/createStore"

const useAppStore = (selector) => {
  const storeCtx = useContext(AppContext);
  const state = useSyncExternalStore(storeCtx.subscribe, () => {
    return selector
    ? selector?.(storeCtx.getState())
    : storeCtx.getState()
  })

  return [state, storeCtx.setState];
}

export default useAppStore;