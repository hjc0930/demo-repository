import { forwardRef, useImperativeHandle, useState } from "react";

const Child2 = forwardRef((_, ref) => {
  const [count, setCount] = useState(0);

  const handleAdd = () => {
    setCount((val) => val + 1);
  };

  useImperativeHandle(ref, () => ({
    handleAdd,
  }));

  return <div>Child2 - {count}</div>;
});

export default Child2;
