import logo from "./logo.svg";
import { Button } from "@hjc0930/component-demo";

import "./App.css";
import "@hjc0930/component-demo/umd/style.min.css";

const request = require.context("./components", true, /\.css$/);

const obj = {};

request.keys().forEach((fileName: any) => {
  console.log(fileName);

  obj[fileName] = request(fileName);
});

console.log(obj);

function App() {
  return (
    <div className="App">
      <Button>Ccc</Button>
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
