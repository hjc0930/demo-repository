import process from "node:process";
import configFactory from "./config/webpack.config.mjs";
import devServerConfig from "./config/webpackServer.config.mjs";
import createCompiler from "./utils/createCompiler.mjs";
import webpack from "webpack";
import WebpackDevServer from "webpack-dev-server";
import logger from "./utils/logger.mjs";
import chalk from "./utils/chalk.mjs";
import path from "node:path";

process.env.BABEL_ENV = "development";
process.env.NODE_ENV = "development";

process.on("unhandledRejection", (err) => {
  throw err;
});

console.log(logger.info(chalk.bright(chalk.cyan("FAST CLI"))));

// const DEFAULT_PORT = parseInt(process.env.PORT, 10) || 3000;
// const HOST = process.env.HOST || "0.0.0.0";

const config = configFactory("development");

const devServer = devServerConfig();

const module = await import(path.resolve(process.cwd(), "./fast.config.mjs"));
const defineConfig = module.default;

const mergeConfig = defineConfig("development", {});

console.log(mergeConfig.server);
const compiler = createCompiler(config, webpack);

const server = new WebpackDevServer(devServer, compiler);

// Launch WebpackDevServer.
server.startCallback(() => {
  console.log(logger.info("Starting the development server..."));
});

["SIGINT", "SIGTERM"].forEach(function (sig) {
  process.on(sig, function () {
    server.close();
    process.exit();
  });
});

if (process.env.CI !== "true") {
  // Gracefully exit when stdin ends
  process.stdin.on("end", function () {
    server.close();
    process.exit();
  });
}
