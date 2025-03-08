import { Link, Outlet } from "react-router";

const Layout = () => {
  return (
    <div>
      <div>Layout</div>
      <ul>
        <li>
          <Link to="/transaction">Home</Link>
        </li>
        <li>
          <Link to="/transaction/open-item-queue">About</Link>
        </li>
      </ul>
      <Outlet />
    </div>
  );
};

export default Layout;
