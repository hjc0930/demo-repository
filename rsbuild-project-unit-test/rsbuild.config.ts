import { defineConfig } from "@rsbuild/core";
import { pluginReact } from "@rsbuild/plugin-react";
import { fileURLToPath, URL } from "node:url";

export default defineConfig({
  plugins: [pluginReact()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
});
