import { defineConfig } from "vite";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";
import react from "@vitejs/plugin-react";
import packageFile from "./package.json";

const __dirname = dirname(fileURLToPath(import.meta.url));

const createExternalRegExp = (dependencie: string) => {
  return new RegExp(`^${dependencie}($|/)`);
};

export default defineConfig({
  plugins: [react()],
  build: {
    lib: {
      entry: resolve(__dirname, "src/index.ts"),
      name: "Demo",
      // 将添加适当的扩展名后缀
      fileName: "Demo",
    },
  },
});
