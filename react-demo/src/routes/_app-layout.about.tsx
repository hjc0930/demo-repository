import { createFileRoute } from "@tanstack/react-router";
import About from "@/views/about";

export const Route = createFileRoute("/_app-layout/about")({
  component: About,
  staticData: {
    title: "关于",
  },
});
