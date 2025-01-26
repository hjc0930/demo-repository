import { fileURLToPath, URL } from "node:url";
import { defineConfig, mergeConfig } from "vitest/config";
import viteConfig from "./vite.config";

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      globals: true, // Enable global variables
      environment: "jsdom", // or 'node' based on your needs
      setupFiles: ["./setup.ts"],
      include: ["test/**/*.{test,spec}.?(c|m)[jt]s?(x)"],
      coverage: {
        all: true,
        provider: "v8",
        reporter: ["text", "html", "clover", "json", "lcov"], // Coverage reporters
      },
      alias: {
        "about/Module": fileURLToPath(
          new URL("./mock/About.tsx", import.meta.url)
        ),
      },
    },
  })
);
