import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ReducerContext from "../views/reducer-context";
import ReactContext from "../views/react-context";
import ModalDemo from "../views/modal-demo";
import MessageDemo from "../views/message-demo";

const router = createBrowserRouter([
  {
    path: "",
    element: <ReducerContext />,
  },
  {
    path: "/react-context",
    element: <ReactContext />,
  },
  {
    path: "modal-demo",
    element: <ModalDemo />,
  },
  {
    path: "message-demo",
    element: <MessageDemo />,
  },
]);

function Router() {
  return <RouterProvider router={router} />;
}

export default Router;
