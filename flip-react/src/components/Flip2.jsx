import { startTransition, useRef, useState } from "react";
import "./flip2.css";
import { Flip } from "./flip.js";

const Flip2 = () => {
  const [list, setList] = useState([
    {
      id: 1,
      name: "HTML",
    },
    {
      id: 2,
      name: "CSS",
    },
    {
      id: 3,
      name: "JavaScript",
    },
    {
      id: 4,
      name: "TypeScript",
    },
  ]);

  const ref = useRef(null);

  const onAdd = () => {
    const flip = new Flip(ref.current.children);
    setList((list) => {
      const id = list.length ? Math.max(...list.map((item) => item.id)) + 1 : 0;
      return [
        {
          id,
          name: `aaa ${id}`,
        },
        ...list,
      ];
    });

    requestAnimationFrame(() => {
      flip.play();
    });
  };

  const onDelete = (id, e) => {
    e.currentTarget.classList.add("item-delete");

    e.currentTarget.addEventListener("animationend", () => {
      const flip = new Flip(ref.current.children);
      setList((list) => list.filter((item) => item.id !== id));
      setTimeout(() => {
        flip.play();
      }, 0);
    });
  };

  return (
    <>
      <button onClick={onAdd}>Add</button>
      <ul className="container" ref={ref}>
        {list.map((item) => (
          <li
            key={item.id}
            className={[
              "item",
              "item-display",
              // isDeleteId === item.id && "item-delete",
            ]
              .filter(Boolean)
              .join(" ")
              .trim()}
            onClick={(e) => {
              onDelete(item.id, e);
            }}
          >
            <span>{item.name}</span>
          </li>
        ))}
      </ul>
    </>
  );
};

export default Flip2;
