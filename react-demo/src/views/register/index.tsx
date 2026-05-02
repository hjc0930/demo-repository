import { Link } from "@tanstack/react-router";

function RegisterPage() {
  return (
    <div className="p-4">
      <h1>Register</h1>
      <Link to="/login">去登录</Link>
    </div>
  );
}

export default RegisterPage;
