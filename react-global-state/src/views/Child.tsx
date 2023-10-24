import { memo, useContext } from "react";
import { AppContext, useAppStore } from "../App";

const Child = memo(() => {
  const [name, setQuery] = useAppStore((state) => state.name);

  console.log(123);

  return (
    <div>
      <h1>Child</h1>
      <p>{name}</p>
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

export default Child;
