import { useState } from "react";
import { add } from "./utils/add";

const App = () => {
  const [value, setValue] = useState("");
  return (
    <>
      <h1>React App</h1>
      <input
        type="text"
        value={value}
        onChange={(e) => setValue(e.target.value)}
      />
      <div>{add(1, 2)}</div>
    </>
  );
};

export default App;
