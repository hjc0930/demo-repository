/**
 * @type {import("jest").Config}
 */
const config = {
  roots: ["<rootDir>"],
  testEnvironment: "jsdom",
  testTimeout: 30000,
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json"],
  // transform
  transformIgnorePatterns: ["/node_modules/(?!)"],
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
    "^.+\\.mjs$": [
      "@swc/jest",
      {
        jsc: {
          parser: {
            syntax: "typescript",
            tsx: true,
            decorators: false,
          },
        },
      },
    ],
  },
  testMatch: [
    "<rootDir>/test/**/__tests__/**/*.{spec,test}.{ts,tsx}",
    "<rootDir>/test/**/*.{spec,test}.{ts,tsx}",
  ],
  testPathIgnorePatterns: [
    "/node_modules/",
    "<rootDir>/config/",
    "<rootDir>/mock/",
    "<rootDir>/.umirc.test.ts",
  ],
  setupFilesAfterEnv: ["<rootDir>/jest.setup.js"],
  moduleNameMapper: {
    "@/(.*)$": "<rootDir>/src/$1",
    "^.+\\.(css|less|sass|scss|stylus)$": "identity-obj-proxy",
  },
  modulePathIgnorePatterns: [
    "<rootDir>/packages/.+/compiled",
    "<rootDir>/packages/.+/fixtures",
  ],
  collectCoverageFrom: [
    "src/**/*.{ts,tsx}",
    "!src/**/*.d.ts",
    "!src/main.tsx",
    "!src/**/index.ts",
  ],
};

module.exports = config;
