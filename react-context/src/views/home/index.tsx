import "./index.css";
import Child1 from "./Child1";
import Child2 from "./Child2";
import { useRef } from "react";

const Home = () => {
  const ref2 = useRef<{ handleAdd: () => void }>(null);

  return (
    <>
      <Child1 ref={ref2} />
      <Child2 handleAdd={ref2?.current?.handleAdd} />
    </>
  );
};

export default Home;
