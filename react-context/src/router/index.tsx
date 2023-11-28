import {
  createBrowserRouter as reactCreateBrowserRouter,
  RouteObject,
  RouterProvider,
} from "react-router-dom";

type reactCBRType = typeof reactCreateBrowserRouter;

const parsePath = (pathKey: string): string | undefined => {
  const splitResult = pathKey.split("/").at(-2);
  if (splitResult === "home") {
    return "";
  }
  return splitResult;
};

function createBrowserRouter(
  opts?: Parameters<reactCBRType>[1]
): ReturnType<reactCBRType> {
  const viewsGlob: Record<string, any> = import.meta.glob(
    ["@/views/**/index.tsx", "@/views/index.tsx"],
    {
      eager: true,
    }
  );

  const response = Object.entries(viewsGlob).map<RouteObject>(
    ([key, module]) => {
      const path = parsePath(key);
      const Default = module.default;
      /**
       * TODO: 约定式路由未实现的功能:
       * 1.路由懒加载的实现
       * 2. loader函数的实现
       * 3.嵌套路由的实现
       */
      // const context = module.context;
      // const loader = module.loader;

      return {
        path,
        element: <Default />,
      };
    }
  );

  return reactCreateBrowserRouter(response, opts);
}

function Router() {
  return <RouterProvider router={createBrowserRouter()} />;
}

export default Router;
