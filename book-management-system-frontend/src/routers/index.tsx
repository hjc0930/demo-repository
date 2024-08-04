import { RouterProvider, createBrowserRouter } from "react-router-dom";
import Login from "../pages/Login";
import Register from "../pages/Register";
import BookManage from "../pages/BookManage";

const routes = [
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/",
    element: <BookManage />,
  },
];

const router = createBrowserRouter(routes);

const Routers = () => {
  return <RouterProvider router={router} />;
};

export default Routers;
