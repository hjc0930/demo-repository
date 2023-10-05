import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";
import Layout from "@/layouts/index.vue";

export type ChildrenType = (RouteRecordRaw & { iconName: string } & Record<
    string,
    any
  >)[];

export const routes: RouteRecordRaw[] = [
  {
    path: "/",
    component: Layout,
    children: [
      {
        path: "/",
        name: "Home",
        iconName: "House",
        component: () => import("@/views/Home.vue"),
      },
      {
        path: "/user",
        name: "User",
        iconName: "User",
        component: () => import("@/views/User.vue"),
      },
    ] as ChildrenType,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
