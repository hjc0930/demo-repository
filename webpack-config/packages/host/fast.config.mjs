import { defineConfig } from "fast-cli";
import { fileURLToPath, URL } from "node:url";

export default defineConfig(() => {
  return {
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
    server: {
      host: "0.0.0.0",
      port: 3001,
    },
  };
});
