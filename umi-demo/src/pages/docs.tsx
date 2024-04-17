import React, { ForwardRefRenderFunction } from "react";

interface MyComponentProps<T> {
  data: T;
}

const MyComponent: ForwardRefRenderFunction<
  HTMLDivElement,
  MyComponentProps<any>
> = (props, ref) => {
  return <div ref={ref}>{props.data}</div>;
};

const MyComponentWithRef = React.forwardRef(MyComponent);

export default MyComponentWithRef;

const a = () => {
  return <MyComponentWithRef data="Hello" />;
};
