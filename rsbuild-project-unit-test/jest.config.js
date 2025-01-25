/** @type {import("jest").Config} */
module.exports = {
  transform: {
    "^.+\\.(js|jsx|ts|tsx)$": [
      "babel-jest",
      {
        presets: [
          "@babel/preset-env",
          [
            "@babel/preset-react",
            {
              runtime: "automatic",
            },
          ],
          "@babel/preset-typescript",
        ],
      },
    ],
  },
  testEnvironment: "jsdom",
  setupFilesAfterEnv: ["<rootDir>/setupTests.ts"],
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json", "node"],
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy",
    "^@/(.*)$": "<rootDir>/src/$1",
  },
  testPathIgnorePatterns: ["/node_modules/", "/dist/", "coverage"],
  transformIgnorePatterns: [
    "/node_modules/",
    "^.+\\.module\\.(css|sass|scss)$",
  ],
  coverageProvider: "v8",
  coverageReporters: ["html", "text", "json", "clover", "lcov"],
};
