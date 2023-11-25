import { Form } from "antd";

const AntdForm = () => {
  const [form] = Form.useForm();

  console.log(form);

  return <div>Ant Form Demo</div>;
};

export default AntdForm;
