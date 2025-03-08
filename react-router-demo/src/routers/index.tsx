import { createBrowserRouter, RouteObject, RouterProvider } from "react-router";
import useUserStore from "@/stores/useGlobalStore";
import routeObject from "./config";
import { cloneDeep } from "lodash-es";

const Router = () => {
  const authorizationRoutersFactory = useUserStore(
    (state) => state.authorizationRoutersFactory
  );
  const authorizationRouters = authorizationRoutersFactory(
    cloneDeep(routeObject)
  );

  if (!authorizationRouters.length) return null;
  const browserRouter = createBrowserRouter(
    authorizationRouters as RouteObject[]
  );
  return <RouterProvider router={browserRouter} />;
};

export default Router;
/**
 * App
 * Router
 *  Login
 *  Layout
 *   Home
 *   About
 *   ...
 *
 */
