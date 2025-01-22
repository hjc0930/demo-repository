import { lazy, Suspense } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const getRemote = (factory: any) => {
  const Remote = lazy(factory);

  return (
    <Suspense>
      <Remote />
    </Suspense>
  );
};

const router = createBrowserRouter([
  {
    path: "/",
    element: <h1>Home</h1>,
  },
  {
    path: "/about",
    element: getRemote(() => import("About/module")),
  },
]);

const Router = () => {
  return <RouterProvider router={router} />;
};

export default Router;
