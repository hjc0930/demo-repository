import { forwardRef, useImperativeHandle, useState } from "react";

export interface ChildProps {
  msg?: string;
}

export interface ChildRef {
  handleAdd: () => void;
}

const Child = forwardRef<ChildRef, ChildProps>((props, ref) => {
  const [count, setCount] = useState(0);

  const handleAdd = () => {
    setCount((val) => val + 1);
  };

  useImperativeHandle(ref, () => ({
    handleAdd,
  }));

  return (
    <div>
      <p>{count}</p>
    </div>
  );
});

export default Child;
