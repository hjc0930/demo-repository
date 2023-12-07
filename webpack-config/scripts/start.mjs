import process from "node:process";
import configFactory from "./config/webpack.config.mjs";
import devServerConfig from "./config/webpackServer.config.mjs";
import createCompiler from "./utils/createCompiler.mjs";
import webpack from "webpack";
import WebpackDevServer from "webpack-dev-server";
import logger from "./utils/logger.mjs";
import chalk from "./utils/chalk.mjs";

process.env.BABEL_ENV = "development";
process.env.NODE_ENV = "development";

process.on("unhandledRejection", (err) => {
  throw err;
});

console.log(logger.info(chalk.bright(chalk.cyan("UIKIT CLI"))));
// Tools like Cloud9 rely on this.
const DEFAULT_PORT = parseInt(process.env.PORT, 10) || 3000;
const HOST = process.env.HOST || "0.0.0.0";

const config = configFactory("development");
const serverConfig = {
  ...devServerConfig(),
  host: HOST,
  port: DEFAULT_PORT,
};
const compiler = createCompiler(config, webpack);

const devServer = new WebpackDevServer(serverConfig, compiler);

// Launch WebpackDevServer.
devServer.startCallback(() => {
  console.log(logger.info("Starting the development server..."));
});

["SIGINT", "SIGTERM"].forEach(function (sig) {
  process.on(sig, function () {
    devServer.close();
    process.exit();
  });
});

if (process.env.CI !== "true") {
  // Gracefully exit when stdin ends
  process.stdin.on("end", function () {
    devServer.close();
    process.exit();
  });
}
