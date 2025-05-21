import type { Config } from "jest";

const config: Config = {
  roots: ["<rootDir>"],
  testTimeout: 30000,
  testEnvironment: "jsdom",
  setupFilesAfterEnv: ["<rootDir>/jest.setup.ts"],
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json"],
  // mapper
  moduleNameMapper: {
    "@/(.*)$": "<rootDir>/src/$1",
    "^.+\\.(css|less|sass|scss|stylus)$": "identity-obj-proxy",
  },
  // coverage
  coverageProvider: "v8",
  coverageReporters: ["html", "json", "clover", "lcov"],
  collectCoverageFrom: [
    "src/**/*.{ts,tsx}",
    "!src/**/*.d.ts",
    "!src/main.tsx",
    "!src/index.ts",
    "!src/index.tsx",
  ],
  // transform
  transformIgnorePatterns: ["/node_modules/(?!)"],
  transform: {
    "^.+\\.(t|j)sx?$": [
      "jest-esbuild",
      {
        jsx: "automatic",
        target: "es2020",
      },
    ],
    "^.+\\.mjs$": [
      "jest-esbuild",
      {
        target: "es2020",
      },
    ],
  },
  // match
  testMatch: [
    "<rootDir>/test/**/__tests__/**/*.{spec,test}.{ts,tsx}",
    "<rootDir>/test/**/*.{spec,test}.{ts,tsx}",
  ],
  testPathIgnorePatterns: ["/node_modules/"],

  modulePathIgnorePatterns: [
    "<rootDir>/packages/.+/compiled",
    "<rootDir>/packages/.+/fixtures",
  ],
};

export default config;
