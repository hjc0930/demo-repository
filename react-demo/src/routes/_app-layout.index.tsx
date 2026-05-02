import { createFileRoute } from "@tanstack/react-router";
import Dashboard from "@/views/dashboard";

export const Route = createFileRoute("/_app-layout/")({
  component: Dashboard,
  staticData: {
    title: "首页",
    roles: ["admin"],
  },
});
