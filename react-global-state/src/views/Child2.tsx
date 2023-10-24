import { memo } from "react";
import { useAppStore } from "../App";

const Child2 = memo(() => {
  const [name, setQuery] = useAppStore((state) => state.name);

  console.log(123);

  return (
    <div>
      <h1>Child2</h1>
      <p>Name: {name}</p>
      <button
        onClick={() => {
          setQuery({
            name: "cccc",
          });
        }}
      >
        Add2
      </button>
    </div>
  );
});

export default Child2;
