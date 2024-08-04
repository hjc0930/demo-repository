import { useRequest } from "ahooks";
import { Button, Card, Form, Input, message } from "antd";
import { Link } from "react-router-dom";
import { authServices } from "../services";

const Login = () => {
  const [api, contextHolder] = message.useMessage();
  const loginRequest = useRequest(authServices.login, { manual: true });

  const onLogin = async (values: any) => {
    await loginRequest.runAsync({
      username: values.username,
      password: values.password,
    });
    api.success("Login successfully");
  };
  return (
    <>
      {contextHolder}
      <Card
        title="Login"
        style={{
          maxWidth: "60%",
          margin: "auto",
          marginTop: "2%",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
        }}
      >
        <Form autoComplete="off" layout="vertical" onFinish={onLogin}>
          <Form.Item label="Username" name="username">
            <Input placeholder="Enter" />
          </Form.Item>
          <Form.Item label="Password" name="password">
            <Input.Password placeholder="Enter" iconRender={() => null} />
          </Form.Item>
          <Form.Item>
            <Link to="/register">Sign up</Link>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              Login
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </>
  );
};

export default Login;
