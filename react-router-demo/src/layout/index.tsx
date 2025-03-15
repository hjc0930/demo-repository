import { getUser } from "@/services/user";
import useGlobalStore from "@/stores/useGlobalStore";
import { useRequest } from "ahooks";
import { Spin } from "antd";
import { Link, Outlet } from "react-router";

const Layout = () => {
  const setUserInfo = useGlobalStore((state) => state.setUserInfo);
  const authorizationRoutersFactory = useGlobalStore(
    (state) => state.authorizationRoutersFactory
  );

  const { loading } = useRequest(async () => {
    const response: any = await getUser();
    setUserInfo(response?.data);
    authorizationRoutersFactory();

    return response?.data;
  });
  return (
    <Spin spinning={loading}>
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
    </Spin>
  );
};

export default Layout;
