import { ReactNode } from "react";
import {
  createBrowserRouter,
  Outlet,
  type RouteObject,
  RouterProvider,
} from "react-router";

type BaseRouterType = {
  name?: string;
  icon?: ReactNode;
  children?: BaseRouterType[];
} & RouteObject;

const routerConfig: BaseRouterType[] = [
  {
    path: "/",
    element: <Outlet />,
    children: [
      {
        index: true,
        element: <>Home</>,
      },
      {
        path: "about",
        element: <>About</>,
      },
    ],
  },
];

const Router = () => {
  return <RouterProvider router={createBrowserRouter(routerConfig)} />;
};

export default Router;
