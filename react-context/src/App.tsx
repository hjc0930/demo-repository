import { Form, Select, SelectProps } from "antd";
import Router from "./router";
import { useRequest } from "ahooks";
import { useEffect } from "react";

interface DataRes {
  id: number;
  name: string;
  status: string;
}

const CustomSelect = (props: SelectProps) => {
  const { data = [], loading } = useRequest(async () => {
    await new Promise((resolve) => setTimeout(resolve, 5000));
    const response = await fetch("http://localhost:3000/list");

    return response.json() as Promise<DataRes[]>;
  });

  return <Select {...props} options={data} loading={loading} />;
};

const App = () => {
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldsValue({
      select: 1,
    });
  }, []);

  return (
    <Form form={form}>
      <Form.Item label="Select" name="select">
        <CustomSelect />
      </Form.Item>
    </Form>
  );
};

export default App;
