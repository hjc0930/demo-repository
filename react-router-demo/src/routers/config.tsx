import Home from "@/views/Home";
import About from "@/views/About";
import { ProjectRouteObject } from "@/entities/common";
import Layout from "@/layout";
import AppInitialization from "@/components/AppInitialization";
import Login from "@/views/Login";

/**
 * 权限管理:
 *  1. 路由(页面)权限管理
 *    - 路由权限管理要实现至少两种功能：
 *        1. 根据返回的权限信息生成最终的渲染路由表，没有权限的路由直接不显示在路由表中
 *        2. 根据返回的token信息，判断用户是停留在主页还是登陆页面，比如: 主页面没有权限就跳转到登录页面，有权限就显示主页面，或者跳转到404页面
 *  2. 页面内权限管理
 *   - 页面内权限管理包括了页面内的按钮、表单、表格等元素的权限控制，这部分权限控制一般是通过权限信息来控制元素的显示与隐藏
 *     1. 通过权限信息来控制页面内元素的显示与隐藏，这一部分可以通过封装一个自定义组件来实现
 */
const routeObject: ProjectRouteObject[] = [
  {
    path: "login",
    element: <Login />,
  },
  {
    path: "transaction",
    element: (
      <AppInitialization>
        <Layout />
      </AppInitialization>
    ),
    children: [
      {
        index: true,
        element: <Home />,
      },
      {
        path: "open-item-queue",
        element: <About />,
        meta: {
          permissions: ["admin1"],
        },
      },
      {
        path: "*",
        element: <div>404</div>,
      },
    ],
  },
  {
    path: "*",
    element: <div>404</div>,
  },
];

export default routeObject;
