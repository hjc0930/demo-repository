import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    "process.env": {
      NODE_ENV: "production",
    },
  },
  build: {
    copyPublicDir: false,
    outDir: "dist",
    lib: {
      entry: "./index.ts",
      name: "MyComponentLibrary",
      fileName: () => `my-component-library.min.js`,
      cssFileName: `my-component-library.min`,
      formats: ["umd"],
    },
    rollupOptions: {
      output: {
        globals: {
          react: "React",
          "react-dom": "ReactDOM",
        },
      },
    },
  },
});
