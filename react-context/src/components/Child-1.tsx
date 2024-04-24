import { HeadContext } from "@/App";
import { useContext } from "react";

const Child1 = () => {
  const count = useContext(HeadContext);

  console.log("Child1");
  return <div>{count}</div>;
};

export default Child1;
