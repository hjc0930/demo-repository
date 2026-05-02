import { createFileRoute } from "@tanstack/react-router";
import AppLayout from "@/layout";
import { queryOptions } from "@tanstack/react-query";
import { getUserInfo } from "@/services/auth";

export const Route = createFileRoute("/_app-layout")({
  component: AppLayout,
  loader: ({ context }) => {
    return context.queryClient.ensureQueryData(
      queryOptions({
        queryKey: ["getUserInfo"],
        queryFn: () => getUserInfo(),
      }),
    );
  },
});
