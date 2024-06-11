import { useEffect, useRef, useState } from "react";

const ProgressView = () => {
  const parentElementRef = useRef<HTMLDivElement>(null);
  const divElementRef = useRef<HTMLDivElement>(null);
  const isDownRef = useRef(false);
  const [position, setPosition] = useState(0);

  const onMouseUp = () => {
    divElementRef.current?.blur();
    isDownRef.current = false;
  };
  const onMouseDown = () => {
    isDownRef.current = true;
  };

  const onMouseMove = (e: any) => {
    e.preventDefault();

    if (isDownRef.current) {
      const parentBoundary = parentElementRef.current?.getBoundingClientRect();
      const targetBoundary = divElementRef.current?.getBoundingClientRect();
      const x = e.clientX - (parentBoundary as any)?.left;

      if (x < 0) {
        setPosition(0);
      } else if (
        x >=
        (parentBoundary?.width ?? 0) - (targetBoundary?.width ?? 0)
      ) {
        setPosition(
          (parentBoundary?.width ?? 0) - (targetBoundary?.width ?? 0)
        );
      } else {
        setPosition(x);
      }
    }
  };

  useEffect(() => {
    document.addEventListener("mousemove", onMouseMove);
    document.addEventListener("mouseup", onMouseUp);
    document.addEventListener("dragstart", function (event) {
      event.preventDefault();
    });

    return () => {
      document.removeEventListener("mouseup", onMouseUp);
      document.removeEventListener("mousemove", onMouseMove);
    };
  }, []);

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: 400,
      }}
    >
      <div
        ref={parentElementRef}
        style={{
          padding: "8px",
          position: "relative",
        }}
      >
        <div
          style={{
            width: "200px",
            height: "8px",
            backgroundColor: "gray",
            borderRadius: "5px",
          }}
        ></div>
        <div
          ref={divElementRef}
          onMouseDown={onMouseDown}
          style={{
            position: "absolute",
            cursor: "pointer",
            left: position,
            top: "50%",
            transform: "translateY(-50%)",
            width: "20px",
            height: "20px",
            borderRadius: "50%",
            backgroundColor: "blue",
          }}
        ></div>
      </div>
    </div>
  );
};

export default ProgressView;
