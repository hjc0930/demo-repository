import { forwardRef, useImperativeHandle, useState } from "react";

const Child1 = forwardRef((_, ref) => {
  const [count, setCount] = useState(0);

  const handleAdd = () => {
    setCount((val) => val + 1);
  };

  useImperativeHandle(ref, () => ({
    handleAdd,
  }));

  return <div onClick={() => handleAdd?.()}>Child1 - {count}</div>;
});

export default Child1;
