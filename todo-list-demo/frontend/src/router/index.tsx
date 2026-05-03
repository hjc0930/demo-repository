import { createBrowserRouter, RouterProvider } from "react-router";

const router = createBrowserRouter([], { basename: import.meta.env.BASE_URL });

const Router = () => {
  return <RouterProvider router={router} />;
};

export default Router;
