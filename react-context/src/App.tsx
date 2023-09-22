import { createContext, memo, useContext, useMemo, useState } from "react";

const AContext = createContext<any>(null);

const Child1 = memo(() => {
  const { num } = useContext(AContext);

  console.log("Child1 Render");

  return (
    <div>
      Child1
      <h1>{num}</h1>
    </div>
  );
});

const Child2 = memo(() => {
  const { theme } = useContext(AContext);

  console.log("Child2 Render");
  return (
    <div>
      Child2
      <h1>{theme}</h1>
    </div>
  );
});

const App = () => {
  const [num, setNum] = useState(0);
  const [theme] = useState(0);

  console.log(Child2);

  return (
    <>
      <button onClick={() => setNum((val) => val + 1)}>Add Num</button>
      <AContext.Provider value={{ num, theme }}>
        {useMemo(
          () => (
            <Child1 />
          ),
          [num]
        )}
        {useMemo(
          () => (
            <Child2 />
          ),
          [theme]
        )}
      </AContext.Provider>
    </>
  );
};

export default App;
