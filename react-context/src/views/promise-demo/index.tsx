import { Button, Form, Input } from "antd";

function Child({ name, rules = [] }: any) {
  console.log(rules);

  const onClick = () => {
    if (!name) return;
    console.log({ onClick: rules });
  };

  return <div onClick={onClick}>Child</div>;
}

function PromiseDemo() {
  return (
    <Child name="Child" />
    // <Form
    //   onFinish={(values) => {
    //     console.log(values);
    //   }}
    // >
    //   <Form.Item label="Username" name="user">
    //     <Input />
    //   </Form.Item>
    //   <Form.Item>
    //     <Button htmlType="submit">Submit</Button>
    //   </Form.Item>
    // </Form>
  );
}

export default PromiseDemo;
