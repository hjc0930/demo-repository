import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ReducerContext from "../views/reducer-context";
import A from "../views/a";

const router = createBrowserRouter([
  {
    path: "",
    element: <ReducerContext />,
  },
  {
    path: "a",
    element: <A />,
  },
]);

function Router() {
  return <RouterProvider router={router} />;
}

export default Router;
