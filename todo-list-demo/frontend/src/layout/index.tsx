import { Outlet } from "react-router";
import { AppBar, Toolbar, Typography, Container } from "@mui/material";
import { Checklist } from "@mui/icons-material";

/**
 * 布局组件：顶部导航 + 内容区
 */
export function Layout() {
  return (
    <>
      <AppBar position="static" elevation={1}>
        <Toolbar>
          <Checklist sx={{ mr: 1 }} />
          <Typography variant="h6">Todo List</Typography>
        </Toolbar>
      </AppBar>

      <Container
        component="main"
        disableGutters
        maxWidth={false}
        sx={{ minHeight: "calc(100vh - 64px)" }}
      >
        <Outlet />
      </Container>
    </>
  );
}
