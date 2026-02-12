import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { worker } from "../mocks/browser";

const bootstrap = async () => {
  if (import.meta.env.VITE_MWS_STATUS === "enable") {
    // 在开发环境中启动 MSW Worker
    await worker.start();
  }
  // 渲染 React 应用
  createRoot(document.getElementById("root")!).render(
    <StrictMode>
      <App />
    </StrictMode>,
  );
};

void bootstrap();
