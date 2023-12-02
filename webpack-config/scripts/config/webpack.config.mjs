import fs from "node:fs";
import path from "node:path";
import process from "node:process";
import { fileURLToPath } from "node:url";

const appDirectory = fs.realpathSync(process.cwd());
/**
 * @param {string} relativePath
 */
const resolveApp = (relativePath) => path.resolve(appDirectory, relativePath);
const appPath = resolveApp(".");
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/**
 * @param {"development" | "production"} env
 * @returns {import("webpack").Configuration}
 */
const configFactory = (env) => {
  const isEnvDevelopment = env === "development";
  const isEnvProduction = env === "production";

  return {
    stats: "errors-warnings",
    mode: isEnvProduction ? "production" : isEnvDevelopment && "development",
    output: {
      clean: true,
      path: path.resolve(appPath, "./dist"),
      filename: isEnvProduction
        ? "js/[name].[contenthash:8].js"
        : isEnvDevelopment && "js/bundle.js",
      chunkFilename: isEnvProduction
        ? "js/[name].[contenthash:8].chunk.js"
        : isEnvDevelopment && "js/[name].chunk.js",
      assetModuleFilename: "assets/[name].[hash][ext]",
    },
    cache: {
      type: "filesystem",
      version: "1.0.0",
      store: "pack",
      buildDependencies: {
        defaultWebpack: ["webpack/lib/"],
        config: [__filename],
      },
    },
  };
};

export default configFactory;
