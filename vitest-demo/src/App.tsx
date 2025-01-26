import { act, useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { Button, Dropdown } from "antd";
import { useEffect } from "react";
import { getAppData } from "./services/app";

function App() {
  const [count, setCount] = useState(0);
  const [list, setList] = useState<any[]>([]);

  useEffect(() => {
    (async () => {
      const res = await getAppData();
      setList(res);
    })();
  }, []);

  return (
    <>
      <input type="file" />
      <ul>
        {list.map((item) => (
          <li key={item.id}>{item.name}-123123</li>
        ))}
      </ul>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <Button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </Button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
      <Dropdown
        menu={{ items: [{ label: <h6>Dropdown First Element</h6>, key: "0" }] }}
      >
        <span>
          Dropdown Click
          <span className="ant-dropdown-open" />
        </span>
      </Dropdown>
    </>
  );
}

export default App;
