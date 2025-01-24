import { Button } from "./components/Button";
import "./App.css";
import { Dropdown } from "antd";

const App = () => {
  return (
    <>
      <Dropdown
        menu={{
          items: [
            {
              label: "Menu Click",
              key: 1,
            },
          ],
        }}
      >
        <button>Dropdown Trigger</button>
      </Dropdown>
      <div className="content">
        <h1>Rsbuild with React</h1>
        <p>Start building amazing things with Rsbuild.</p>
        <Button>123123</Button>
      </div>
    </>
  );
};

export default App;
