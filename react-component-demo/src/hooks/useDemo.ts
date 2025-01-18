import { useState } from "react";

const useDemo = () => {
  const [count, setCount] = useState(0);

  const increment = () => {
    setCount((val) => val + 1);
  };

  return { count, increment };
};

export default useDemo;
