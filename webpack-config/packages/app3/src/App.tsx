import { useState } from "react";
import A from "@/components/A";
import "./app.css";

const App = () => {
  const [count, setCount] = useState(0);
  return (
    <div>
      <h1>123123222444422</h1>
      <A />
      <input type="text" placeholder="Enter" />
      <button onClick={() => setCount((val) => val + 1)}>Count: {count}</button>
    </div>
  );
};
export default App;
