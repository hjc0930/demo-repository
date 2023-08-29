import { BrowserRouter, useRoutes } from "react-router-dom";
import Home from "@/views/Home";
import About from "@/views/About";

const Router = () => {
  return useRoutes([
    {
      path: "",
      element: <Home />,
    },
    {
      path: "about",
      element: <About />,
    },
  ]);
};

const Routers = () => {
  return (
    <BrowserRouter>
      <Router />
    </BrowserRouter>
  );
};

export default Routers;
