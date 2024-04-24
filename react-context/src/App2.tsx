import { PropsWithChildren, createContext, useContext, useState } from "react";
import { Button } from "antd";

export const HeadContext = createContext<any>(0);

const Child1 = () => {
  const count = useContext(HeadContext);

  console.log("Child1");
  return <div>{count}</div>;
};

const Child2 = () => {
  console.log("Child2");

  return null;
};

const HeadProvider = (props: PropsWithChildren) => {
  const [count, setCount] = useState(0);

  return (
    <>
      <Button onClick={() => setCount((count) => count + 1)}>Add</Button>
      <HeadContext.Provider value={count}>
        {props.children}
      </HeadContext.Provider>
    </>
  );
};

function App() {
  return (
    <>
      <HeadProvider>
        <Child1 />
        <Child2 />
      </HeadProvider>
    </>
  );
}

export default App;
