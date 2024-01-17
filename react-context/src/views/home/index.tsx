import { Button, Form, Input } from "antd";

function Home() {
  const [form] = Form.useForm();

  const getData = () => {
    console.log(form.getFieldValue("name"));
  };

  return (
    <>
      <Button onClick={getData}>Get</Button>
      <Form form={form}>
        <Form.Item label="Name" name="name" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
        </Form.Item>
      </Form>
    </>
  );
}

export default Home;
