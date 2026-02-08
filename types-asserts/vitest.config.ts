import { defineConfig } from "vitest/config";

export default defineConfig(() =>
  defineConfig({
    test: {
      environment: "jsdom",
      coverage: {
        provider: "v8",
        reporter: ["text", "json", "html"],
        include: ["src/**/*.{ts,tsx}"],
        exclude: ["node_modules/**/*", "**/*/types.ts"],
        all: true,
      },
    },
  })
);
