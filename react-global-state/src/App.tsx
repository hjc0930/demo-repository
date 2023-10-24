import createStoreContext from "./customStore/createStoreContext";
import Child from "./views/Child";
import Child2 from "./views/Child2";
import Home from "./views/Home";

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

export { useAppStore };

function App() {
  return (
    <AppContextProvider>
      <Home />
      <Child />
      <Child2 />
    </AppContextProvider>
  );
}

export default App;
