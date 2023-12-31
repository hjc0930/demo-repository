import { realpathSync } from "node:fs";
import process from "node:process";
import { resolve } from "node:path";

const moduleFileExtensions = [
  "web.mjs",
  "mjs",
  "web.js",
  "js",
  "web.ts",
  "ts",
  "web.tsx",
  "tsx",
  "json",
  "web.jsx",
  "jsx",
];
const appDirectory = realpathSync(process.cwd());
/**
 * @param {string} relativePath
 */
const resolveApp = (relativePath) => resolve(appDirectory, relativePath);

const appPath = resolveApp(".");
const appSrc = resolveApp("src");
const appDist = resolveApp("dist");
const appPublic = resolveApp("public");
const appEntry = resolveApp("src/index");
const appHtml = resolveApp("public/index.html");
const appNodeModules = resolveApp("node_modules");
const appPackageJson = resolveApp("package.json");
const appWebpackCatch = resolveApp("node_modules/.cache");
const externalConfigPath = resolveApp("uds.config.mjs");

export {
  appPath,
  appSrc,
  appEntry,
  appDist,
  appHtml,
  appPublic,
  appNodeModules,
  appPackageJson,
  appWebpackCatch,
  externalConfigPath,
  moduleFileExtensions,
};
