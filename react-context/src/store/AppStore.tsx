import {
  PropsWithChildren,
  createContext,
  useCallback,
  useContext,
  useReducer,
  Dispatch,
} from "react";

const initaleState = {
  count: 0,
};
const reducer = (state: typeof initaleState, action: typeof initaleState) => {
  return {
    ...state,
    ...action,
  };
};

const AppContext = createContext(initaleState);
const AppDispatchContext = createContext<Dispatch<typeof initaleState>>(
  () => {}
);

function AppStore({ children }: PropsWithChildren) {
  const [store, dispatch] = useReducer(reducer, initaleState);

  return (
    <AppContext.Provider value={store}>
      <AppDispatchContext.Provider value={dispatch}>
        {children}
      </AppDispatchContext.Provider>
    </AppContext.Provider>
  );
}

function useAppStore() {
  return useContext(AppContext);
}

function useAppDispatch() {
  return useCallback(useContext(AppDispatchContext), []);
}

export { useAppStore, useAppDispatch };
export default AppStore;
