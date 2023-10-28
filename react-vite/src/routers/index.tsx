import { RouterProvider, createBrowserRouter } from "react-router-dom";

console.log(
  import.meta.glob("../views/**.tsx", {
    eager: true,
    // import: "Component",
  })
);

// const renderRouter = Object.entries(import.meta.glob("../views/**")).reduce(
//   (prev: any[], [key, lazy]) => {
//     const paths = key.toLocaleLowerCase().split("/");
//     const pathLastButOne = paths.at(-2);
//     let path = paths.at(-1)?.split(".")[0];

//     if (pathLastButOne !== "views") {
//       path = `${paths.at(-2)}/${path}`;
//     }

//     return [
//       ...prev,
//       {
//         path: path === "home" ? "" : path,
//         lazy,
//       },
//     ];
//   },
//   []
// );

// const router = createBrowserRouter(renderRouter);
const router = createBrowserRouter([
  {
    path: "",
    loader: () => {
      document.title = "Home";
      return {
        a: 1,
        b: 2,
      };
    },
    lazy: () => import("../views/home"),
  },
  {
    path: "about",
    loader: () => {
      document.title = "About";
      return {
        a: 1,
        b: 2,
      };
    },
    lazy: () => import("../views/about"),
  },
]);

const Routers = () => <RouterProvider router={router} />;

export default Routers;
