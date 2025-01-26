import { lazy, Suspense } from "react";
import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
} from "react-router-dom";

const getRemote = (factory: () => Promise<{ default: any }>) => {
  const Remote = lazy(factory);
  return (
    <Suspense fallback={<h1>Loading...</h1>}>
      <Remote />
    </Suspense>
  );
};

export const routerConfig = [
  {
    path: "/",
    element: <h1>Home</h1>,
  },
  {
    path: "/about",
    element: getRemote(() => import("about/Module")),
  },
  {
    path: "/app1",
    element: <Navigate to="/about" />,
  },
];

const Router = () => {
  const browserRouter = createBrowserRouter(routerConfig);
  return <RouterProvider router={browserRouter} />;
};

export default Router;
