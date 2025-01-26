import { Button, Dropdown } from "antd";

const App = () => {
  return (
    <>
      <Dropdown menu={[{ key: "1", label: "123123" }]}>
        <Button>Click me</Button>
      </Dropdown>
      <div>
        <h1>Hello world!</h1>
      </div>
    </>
  );
};

export default App;
