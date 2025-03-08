import { Button } from "antd";
import { useNavigate } from "react-router";

const Login = () => {
  const navigate = useNavigate();
  return (
    <Button type="primary" onClick={() => navigate("/transaction")}>
      login
    </Button>
  );
};

export default Login;
