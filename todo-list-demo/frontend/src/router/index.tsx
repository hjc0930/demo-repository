import { createBrowserRouter, RouterProvider } from "react-router";
import { Layout } from "@/layout";
import { TodoListPage } from "@/todo";

const router = createBrowserRouter(
  [
    {
      path: "/",
      element: <Layout />,
      children: [
        { index: true, element: <TodoListPage /> },
        {
          path: "todos",
          element: <TodoListPage />,
        },
      ],
    },
  ],
  { basename: import.meta.env.BASE_URL },
);

const Router = () => {
  return <RouterProvider router={router} />;
};

export default Router;
