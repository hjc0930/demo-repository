import { Button, Card, Form, Input, Space, Typography } from "antd";
import { CloseOutlined } from "@ant-design/icons";

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
