import { createFileRoute } from "@tanstack/react-router";
import RegisterPage from "@/views/register";

export const Route = createFileRoute("/register")({
  component: RegisterPage,
});
