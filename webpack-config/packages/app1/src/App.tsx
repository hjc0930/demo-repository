import { useState } from "react";
import App2 from "app2/App";
import "./app.css";

const App = () => {
  const [count, setCount] = useState(0);
  return (
    <div>
      <h1>App1</h1>
      <input type="text" placeholder="Enter" />
      <button onClick={() => setCount((val) => val + 1)}>Count: {count}</button>
      <App2 />
    </div>
  );
};
export default App;
