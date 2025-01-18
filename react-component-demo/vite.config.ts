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
    target: "modules",
    emptyOutDir: false,
    minify: true,
    lib: {
      entry: resolve(__dirname, "src/index.ts"),
      fileName: "react-component-demo",
      name: "ReactComponentDemo",
      formats: ["es"],
    },
    rollupOptions: {
      external: ["react", "node_modules"],

      output: {
        // 在 UMD 构建模式下为这些外部化的依赖
        // 提供一个全局变量
        globals: Object.keys(packageFile.dependencies || {}).reduce(
          (acc, item) => {
            acc[item] = item;
            return acc;
          },
          {}
        ),
      },

      // external: [
      //   /node_modules/,
      //   ...Object.keys(packageFile.dependencies || {}).map((item) =>
      //     createExternalRegExp(item)
      //   ),
      // ],
      // output: [
      //   {
      //     dir: resolve(__dirname, "dist"),
      //     format: "umd",
      //     name: "demo",
      //     globals: Object.keys(packageFile.dependencies || {}).reduce(
      //       (acc, item) => {
      //         acc[item] = item;
      //         return acc;
      //       },
      //       {}
      //     ),
      //   },
      //   {
      //     dir: resolve(__dirname, "es"),
      //     preserveModules: true,
      //     format: "es",
      //     preserveModulesRoot: "src",
      //     entryFileNames: "[name].js",
      //     chunkFileNames: "[name].js",
      //   },
      //   {
      //     dir: resolve(__dirname, "lib"),
      //     format: "cjs",
      //     preserveModules: true,
      //     preserveModulesRoot: "src",
      //     entryFileNames: "[name].js",
      //     chunkFileNames: "[name].js",
      //   },
      // ],
    },
  },
});
