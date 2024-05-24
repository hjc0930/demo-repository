import { useRequest } from "ahooks";
import { exportExcelByNative, formatJson } from "./utils/exportExcel";
import Item from "./components/Form/Item";
import Form from "./components/Form/Form";

function App() {
  const onSubmit = (values: any) => {
    console.log(values);
  };

  return (
    <Form
      autoComplete="off"
      onSubmit={onSubmit}
      style={{
        display: "inline-flex",
        flexDirection: "column",
        rowGap: "8px",
      }}
    >
      <Item label="UserName" name="username">
        <input />
      </Item>
      <Item label="Password" name="password">
        <input type="password" />
      </Item>
      <Item>
        <button type="submit">Submit</button>
      </Item>
    </Form>
  );
}

export default App;
