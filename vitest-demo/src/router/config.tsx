import { lazy, Suspense } from "react";
import { Navigate, type RouteObject } from "react-router-dom";

const getRemote = (factory: () => Promise<{ default: any }>) => {
  const Remote = lazy(factory);
  return (
    <Suspense fallback={<h1>Loading...</h1>}>
      <Remote />
    </Suspense>
  );
};

const routerConfig: RouteObject[] = [
  {
    path: "/",
    element: <div>Home</div>,
  },
  {
    path: "/about",
    element: <div>About</div>,
  },
  {
    path: "/app1",
    element: <Navigate to="/about" />,
  },
];

export default routerConfig;
