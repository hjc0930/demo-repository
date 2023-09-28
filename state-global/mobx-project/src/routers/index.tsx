import { useRoutes, BrowserRouter } from "react-router-dom";
import Layout from "../layout";
import Home from "../views/Home";
import User from "../views/User";

const Routers = () => useRoutes([
  {
    path: "",
    element: <Layout />,
    children: [
      {
        path: "",
        element: <Home />,
      },
      {
        path: "/user",
        element: <User />
      }
    ]
  }
]);

export default () => {
  return (
    <BrowserRouter>
      <Routers />
    </BrowserRouter>
  );
};
