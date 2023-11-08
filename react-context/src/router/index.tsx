import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ReducerContext from "../views/reducer-context";

const router = createBrowserRouter([
  {
    path: "",
    element: <ReducerContext />,
  },
]);

function Router() {
  return <RouterProvider router={router} />;
}

export default Router;
