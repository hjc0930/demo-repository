import { PropsWithChildren, useEffect, useRef, useState } from "react";
import styles from "./index.module.css";
import { Button } from "@mui/material";
import { Flip as FlipConstructor } from "../../utils/flip";

let uuid = 1;

const Item = ({ children }: PropsWithChildren) => {
  return <li>{children}</li>;
};

function Flip() {
  const containerRef = useRef<HTMLUListElement>(null);
  const flipInstance = useRef<FlipConstructor>();

  const [list, setList] = useState<number[]>([]);

  useEffect(() => {
    flipInstance.current = new FlipConstructor(containerRef.current);
  }, []);

  const onAdd = () => {
    setList((list) => [...list, uuid++]);

    requestAnimationFrame(() => {
      flipInstance.current?.startRectOfrecalculate();
    });
  };

  const onRemove = () => {
    if (!list.length) return;
    setList((list) => list.slice(1));
    requestAnimationFrame(() => {
      flipInstance.current?.play();
    });
  };
  return (
    <>
      <Button onClick={onAdd} variant="outlined">
        Add
      </Button>
      <Button onClick={onRemove} variant="outlined">
        Delete
      </Button>
      <ul ref={containerRef} className={styles.container}>
        {list.map((item) => (
          <Item key={item}>{item}</Item>
        ))}
      </ul>
    </>
  );
}

export default Flip;
