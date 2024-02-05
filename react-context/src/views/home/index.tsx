import { Button, Form, Input } from "antd";

function Home() {
  const [form] = Form.useForm();

  return (
    <Form
      onFinish={(values) => {
        console.log(values);
      }}
    >
      <Form.Item>
        <Input />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit">Add</Button>
      </Form.Item>
    </Form>
  );
}

export default Home;
