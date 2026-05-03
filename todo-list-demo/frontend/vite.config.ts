import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";
import { fileURLToPath, URL } from "node:url";
import * as process from "node:process";

// https://vite.dev/config/
export default defineConfig((env) => {
  const environment = loadEnv(env.mode, process.cwd());

  return {
    base: environment.VITE_BASE_URL,
    plugins: [react()],

    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
  };
});
