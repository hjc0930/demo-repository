import { create } from "zustand";
import { ProjectRouteObject } from "../entities/common";
import routeBoject from "@/routers/config";
import { cloneDeep } from "lodash-es";

type UseGlobalStoreType = {
  authorizationRouters: ProjectRouteObject[];
  userInfo: null | {
    id: number;
    name: string;
    permission: string[];
  };
  setUserInfo: (userInfo: {
    id: number;
    name: string;
    email: string;
    role: string;
    permission: string[];
  }) => void;
  authorizationRoutersFactory: () => ProjectRouteObject[];
};

const authorizationRouterInit = (
  userPermission: string[],
  initialRouter: ProjectRouteObject[]
) => {
  const authorizationRouters: ProjectRouteObject[] = [];

  for (const item of initialRouter) {
    const authRouter: ProjectRouteObject = item;
    if (
      item?.meta?.permissions?.length &&
      !item?.meta?.permissions?.some((permission) =>
        userPermission.includes(permission)
      )
    ) {
      continue;
    }
    if (item?.children?.length) {
      authRouter.children = authorizationRouterInit(
        userPermission,
        item.children
      );
    }
    authorizationRouters.push(authRouter);
  }

  return authorizationRouters;
};

const useGlobalStore = create<UseGlobalStoreType>((set, get) => ({
  userInfo: null,
  authorizationRouters: routeBoject,

  setUserInfo: (userInfo) => set({ userInfo }),
  authorizationRoutersFactory: () => {
    const userPermission = get().userInfo?.permission ?? [];
    const authorizationRouters: ProjectRouteObject[] = authorizationRouterInit(
      userPermission,
      cloneDeep(routeBoject)
    );
    set({ authorizationRouters });
    return authorizationRouters;
  },
}));

export default useGlobalStore;
