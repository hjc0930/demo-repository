import { createFileRoute } from "@tanstack/react-router";
import LoginPage from "@/views/login";

export const Route = createFileRoute("/login")({
  component: LoginPage,
});
