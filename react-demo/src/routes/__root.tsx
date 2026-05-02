import { Box } from "@mui/material";
import type { QueryClient } from "@tanstack/react-query";
import {
  createRootRouteWithContext,
  Link,
  Outlet,
} from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";

export const Route = createRootRouteWithContext<{ queryClient: QueryClient }>()(
  {
    component: () => (
      <>
        <Outlet />
        <TanStackRouterDevtools />
      </>
    ),
    notFoundComponent: () => {
      return (
        <Box>
          <p>This is the notFoundComponent configured on root route</p>
          <Link to="/">Start Over</Link>
        </Box>
      );
    },
  },
);
