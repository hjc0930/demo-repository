import { useRequest } from "ahooks";
import { Button, Card, Form, Input, message } from "antd";
import { Link, useNavigate } from "react-router-dom";
import { authServices } from "../services";

function Register() {
  const navigate = useNavigate();
  const [api, contextHolder] = message.useMessage();
  const registerRequest = useRequest(authServices.register, { manual: true });

  const onRegister = async (values: any) => {
    await registerRequest.runAsync({
      username: values.username,
      password: values.password,
    });
    api.success("Register successfully");

    setTimeout(() => {
      navigate("/login");
    }, 1000);
  };
  return (
    <>
      {contextHolder}
      <Card
        title="Register"
        style={{
          minWidth: "300px",
          maxWidth: "60%",
          margin: "auto",
          marginTop: "2%",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
        }}
      >
        <Form autoComplete="off" layout="vertical" onFinish={onRegister}>
          <Form.Item
            label="Username"
            name="username"
            rules={[{ required: true, message: "Please enter userName" }]}
          >
            <Input placeholder="Enter" />
          </Form.Item>
          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: "Please enter Password" }]}
          >
            <Input.Password placeholder="Enter" iconRender={() => null} />
          </Form.Item>
          <Form.Item
            label="Confirm Password"
            name="confirmPassword"
            dependencies={["password"]}
            rules={[
              { required: true, message: "Please confirm Password" },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue("password") === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(
                    new Error("The new password that you entered do not match!")
                  );
                },
              }),
            ]}
          >
            <Input.Password placeholder="Enter" iconRender={() => null} />
          </Form.Item>

          <Form.Item>
            <Link to="/login">Sign in</Link>
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={registerRequest.loading}
            >
              Register
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </>
  );
}

export default Register;
