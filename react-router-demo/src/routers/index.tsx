import { createBrowserRouter, RouteObject, RouterProvider } from "react-router";
import useGlobalStore from "@/stores/useGlobalStore";

const Router = () => {
  const authorizationRouters = useGlobalStore(
    (state) => state.authorizationRouters
  );

  if (!authorizationRouters.length) return null;
  const browserRouter = createBrowserRouter(
    authorizationRouters as RouteObject[]
  );

  return <RouterProvider router={browserRouter} />;
};

export default Router;
