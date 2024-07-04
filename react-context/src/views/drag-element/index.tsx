import { Button, Modal } from "antd";
import { startTransition, useRef, useState } from "react";

const dragElement = () => {
  const startPosition = useRef({
    x: 0,
    y: 0,
    left: 0,
    top: 0,
  });
  const [position, setPosition] = useState({
    x: 0,
    y: 0,
  });
  const [loading, setLoading] = useState(false);

  const move = (event: MouseEvent) => {
    const offsetX = event.clientX - startPosition.current.x;
    const offsetY = event.clientY - startPosition.current.y;

    const translateX = startPosition.current.left + offsetX;
    const translateY = startPosition.current.top + offsetY;
    startTransition(() => {
      setPosition({
        x: translateX,
        y: translateY,
      });
    });
    // 阻止默认行为
    event.preventDefault();
  };
  const onMouseDown = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
    const target = event.currentTarget as HTMLDivElement;
    const { left, top } = target.getBoundingClientRect();

    startPosition.current.left = left;
    startPosition.current.top = top;
    startPosition.current.x = event.clientX;
    startPosition.current.y = event.clientY;

    document.addEventListener("mousemove", move);
    const up = () => {
      document.removeEventListener("mousemove", move);
      document.removeEventListener("mouseup", up);
      document.removeEventListener("mouseup", up);
    };
    document.addEventListener("mouseup", up);
  };

  const waitTimeout = () => new Promise((resolve) => setTimeout(resolve, 1500));

  const onOpen = () => {
    Modal.confirm({
      title: "Drag Element",
      // okButtonProps: {
      //   loading,
      // },
      onOk: async () => {
        const { promise, resolve } = Promise.withResolvers();
        setTimeout(resolve, 2000);
        return promise;
        // return new Promise((resolve) => {
        //   setTimeout(() => {
        //     resolve(true);
        //   }, 1500);
        // });
        // setLoading(true);
        // await waitTimeout();
        // setLoading(false);
      },
    });
  };
  return (
    <>
      <Button onClick={onOpen}>Confirm</Button>
      <div
        style={{
          maxWidth: "200px",
          border: "1px solid #000",
          cursor: "move",
          transform: `translate3d(${position.x}px, ${position.y}px, 0px)`,
        }}
        onMouseDown={onMouseDown}
      >
        <div
          className="header"
          style={{
            height: "20px",
            background: "#ccc",
          }}
        >
          Header
        </div>
        <div>123123123</div>
      </div>
    </>
  );
};

export default dragElement;
