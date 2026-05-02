import { createRoot } from "react-dom/client";
import { ThemeProvider, CssBaseline, createTheme } from "@mui/material";
import "./index.css";
import App from "./App";

const theme = createTheme();

async function bootstrap() {
  // 启动 Mock 服务（仅开发环境且启用时）
  if (import.meta.env.VITE_ENABLE_MOCK === "true") {
    const { worker } = await import("../mocks/browser");
    await worker.start({
      onUnhandledRequest: "warn",
    });
  }

  createRoot(document.getElementById("root")!).render(
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <App />
    </ThemeProvider>,
  );
}

bootstrap();
