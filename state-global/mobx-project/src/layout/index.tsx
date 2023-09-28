import { useEffect } from "react";
import { Link, Outlet } from "react-router-dom";
import useGlobalStore from "../hooks/useGlobalStore";

const Layout = () => {
  // const { init } = useGlobalStore("user");

  useEffect(() => {
    // init();
    console.log(12321321);
  }, [])
  return (
    <div>
      <h1>Layout</h1>
      <ul>
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/user">User</Link>
        </li>
      </ul>
      <Outlet />
    </div>
  );
};

export default Layout;
