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
      fileName: () => "demo.min.js",
    },
    rollupOptions: {
      external: [
        /node_modules/,
        ...Object.keys(packageFile.dependencies || {}).map((item) =>
          createExternalRegExp(item)
        ),
      ],
      output: [
        {
          dir: resolve(__dirname, "dist"),
          format: "umd",
          name: "Demo",
          globals: Object.keys(packageFile.dependencies || {}).reduce(
            (acc, item) => {
              acc[item] = item;
              return acc;
            },
            {}
          ),
        },
        {
          dir: resolve(__dirname, "es"),
          preserveModules: true,
          format: "es",
          preserveModulesRoot: "src",
          entryFileNames: "[name].js",
          chunkFileNames: "[name].js",
        },
        {
          dir: resolve(__dirname, "lib"),
          format: "cjs",
          preserveModules: true,
          preserveModulesRoot: "src",
          entryFileNames: "[name].js",
          chunkFileNames: "[name].js",
        },
      ],
    },
  },
});
