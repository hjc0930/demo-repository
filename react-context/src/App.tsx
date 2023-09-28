import { createContext } from "react";
import { Store, createStore } from "./stores/createStore";
import useAppStore from "./stores/useAppStores";

export type QueryState = {
  name?: string;
  team?: string;
  age?: number;
  score?: string;
};

export const AppContext = createContext<Store<QueryState> | null>(null);

const initState: QueryState = {
  name: "JiaCheng",
  age: 24,
};
const store = createStore(initState);

const Child1 = () => {
  const [state, setState] = useAppStore((state) => ({ name: state.name }));

  return (
    <div>
      <h1
        onClick={() => {
          setState({
            name: "2222",
          });
        }}
      >
        Child1
      </h1>
      <p>{state.name}</p>
    </div>
  );
};

const Child2 = () => {
  const [age, setAge] = useAppStore((state) => state.age);

  return (
    <div>
      <h1
        onClick={() => {
          setAge({
            age: (age as number) + 1,
          });
        }}
      >
        Child2
      </h1>
      <p>{age as number}</p>
    </div>
  );
};

function App() {
  return (
    <AppContext.Provider value={store}>
      <Child1 />
      <Child2 />
    </AppContext.Provider>
  );
}

export default App;
