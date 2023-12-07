import { useState } from "react";
import "./app.css";
// import fn from "../public/fn";

// console.log(fn);

const App = () => {
  const [count, setCount] = useState(0);
  return (
    <div>
      <h1>123123222444422</h1>
      <input type="text" placeholder="Enter" />
      <button onClick={() => setCount((val) => val + 1)}>Count: {count}</button>
    </div>
  );
};
``;
export default App;
