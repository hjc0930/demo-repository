import { Form, Select } from "antd";
import { debounce } from "lodash-es";
import { useState } from "react";

const App = () => {
  const [form] = Form.useForm();
  const [value, setValue] = useState("");
  const [options, setOptions] = useState<any[]>([]);

  const onSearch = debounce(async (value: string) => {
    await new Promise((resolve) => setTimeout(resolve, 2000));
    if (value === "1") {
      setOptions([
        { label: "Option 1", value: "1" },
        { label: "Option 2", value: "2" },
        { label: "Option 3", value: "3" },
      ]);
    }
    if (value === "2") {
      setOptions([
        { label: "Option 4", value: "4" },
        { label: "Option 5", value: "5" },
        { label: "Option 6", value: "6" },
      ]);
    }
  });

  return (
    <Form form={form}>
      <Form.Item label="Select" name="select">
        <Select
          placeholder="Select"
          value={value}
          onChange={(value) => {
            setValue(value);
          }}
          showSearch
          onSearch={onSearch}
          filterOption={false}
          options={options}
        />
      </Form.Item>
    </Form>
  );
};

export default App;
