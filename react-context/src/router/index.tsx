import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ReducerContext from "../views/reducer-context";
import ReactContext from "../views/react-context";

const router = createBrowserRouter([
  {
    path: "",
    element: <ReducerContext />,
  },
  {
    path: "/react-context",
    element: <ReactContext />,
  },
]);

function Router() {
  return <RouterProvider router={router} />;
}

export default Router;
