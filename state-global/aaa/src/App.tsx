import { createContext, useState, useRef } from "react";
import Child from "./Child";

export const AppContext = createContext<any>({});

type ResultChildRefType<T> = T extends React.ForwardRefExoticComponent<
  React.RefAttributes<infer B>
>
  ? B
  : any;

function App() {
  const [count, setCount] = useState(0);
  const childRef = useRef<ResultChildRefType<typeof Child>>(null);

  return (
    <AppContext.Provider value={{ count, setCount }}>
      <Child ref={childRef} />
    </AppContext.Provider>
  );
}

export default App;
