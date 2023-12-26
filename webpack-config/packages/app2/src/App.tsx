import { useState } from "react";
import "./app.css";

const App = () => {
  const [count, setCount] = useState(0);
  return (
    <div>
      <h1>App2</h1>
      <input type="text" placeholder="Enter" />
      <button onClick={() => setCount((val) => val + 1)}>Count: {count}</button>
    </div>
  );
};
export default App;
