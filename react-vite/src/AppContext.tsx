import createStoreContext from "./hooks/createStoreProvider";

//  AppContext
export type QueryState = {
  name?: string;
  team?: string;
  age?: string;
  score?: string;
};

const initState: QueryState = {
  name: "",
  team: "",
  age: undefined,
  score: undefined,
};

const { StoreProvider: AppContextProvider, useStore: useAppStore } =
  createStoreContext(initState);

export { AppContextProvider, useAppStore };
