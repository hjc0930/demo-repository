import { Button, Dropdown } from "antd";
import { cloneDeep } from "lodash-es";

const App = () => {
  return (
    <>
      <div>{cloneDeep({ a: 1, b: 2 })}</div>
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
