import { ReactNode } from "react";
import type { RouteObject } from "react-router";

export type ProjectRouteObject = Omit<RouteObject, "children"> & {
  meta?: {
    title?: string;
    icon?: ReactNode;
    permissions?: string[];
  };
  children?: ProjectRouteObject[];
};
