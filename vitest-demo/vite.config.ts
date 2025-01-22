/// <reference types="vitest" />
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { fileURLToPath } from "node:url";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom", // or 'node' based on your needs
    coverage: {
      reporter: ["text", "html", "clover", "json", "lcov"], // Coverage reporters
    },
    setupFiles: ["./setup.ts"],
    globals: true, // Enable global variables
    alias: {
      "about/Module": fileURLToPath(
        new URL("./mock/About.tsx", import.meta.url)
      ),
    },
  },
});

// { find: /^app1/, replacement: fileURLToPath(new URL('../app1/src/components', import.meta.url)) }
