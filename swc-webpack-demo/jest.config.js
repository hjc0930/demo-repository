/**
 * @type {import("jest").Config}
 */
const config = {
  roots: ["<rootDir>"],
  testEnvironment: "jest-environment-jsdom",
  moduleFileExtensions: ["ts", "tsx", "js", "json"],
  transform: {
    "^.+\\.(t|j)sx?$": [
      "@swc/jest",
      {
        jsc: {
          parser: {
            syntax: "typescript",
            tsx: true,
            decorators: false,
          },
          transform: {
            react: {
              runtime: "automatic",
            },
          },
        },
      },
    ],
  },
  testMatch: [
    "<rootDir>/test/**/__tests__/**/*.{spec,test}.{ts,tsx}",
    "<rootDir>/test/**/*.{spec,test}.{ts,tsx}",
  ],
  setupFilesAfterEnv: ["<rootDir>/jest.setup.js"],
  collectCoverageFrom: [
    "src/**/*.{ts,tsx}",
    "!src/**/*.d.ts",
    "!src/main.tsx",
    "!src/**/index.ts",
  ],
};

module.exports = config;
