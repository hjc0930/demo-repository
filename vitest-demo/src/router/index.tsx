import { createBrowserRouter, RouterProvider } from "react-router-dom";
import routerConfig from "./config";

const Router = () => {
  const browserRouter = createBrowserRouter(routerConfig);
  return <RouterProvider router={browserRouter} />;
};

export default Router;
