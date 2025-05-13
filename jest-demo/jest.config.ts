/**
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/configuration
 */
import type { Config } from "jest";

const config: Config = {
  collectCoverage: true,
  coverageDirectory: "coverage",
  coverageProvider: "v8",
  coverageReporters: ["json", "text", "lcov", "clover"],
  setupFilesAfterEnv: ["<rootDir>/setup.ts"],
  testEnvironment: "jsdom",
  testPathIgnorePatterns: ["/node_modules/"],
  testMatch: [
    "**/__tests__/**/*.[jt]s?(x)",
    "**/test/**/*.[jt]s?(x)",
    "**/?(*.)+(spec|test).[tj]s?(x)",
  ],
  testTimeout: 60000,
  transformIgnorePatterns: ["/node_modules/(?!)"],
  transform: {
    "^.+\\.[tj]sx?$": [
      "jest-esbuild",
      {
        jsx: "automatic",
        target: "es2020",
      },
    ],
  },
};

export default config;
