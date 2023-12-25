import process from "node:process";
import configFactory from "./config/webpack.config.mjs";
import devServerConfig from "./config/webpackServer.config.mjs";
import createCompiler from "./utils/createCompiler.mjs";
import webpack from "webpack";
import { merge } from "webpack-merge";
import WebpackDevServer from "webpack-dev-server";
import logger from "./utils/logger.mjs";
import chalk from "./utils/chalk.mjs";
import path from "node:path";

process.env.BABEL_ENV = "development";
process.env.NODE_ENV = "development";

process.on("unhandledRejection", (err) => {
  throw err;
});

const start = process.env.START;

start === "first" &&
  console.log(logger.info(chalk.bright(chalk.cyan("FAST CLI"))));

const module = await import(path.resolve(process.cwd(), "./fast.config.mjs"));
const defineConfig = module.default;

const create = () => {
  /**
   * @type {import("..").FastConfig}
   */
  const { server: externalServer = {}, ...externalWebpacConfig } =
    typeof defineConfig === "function"
      ? defineConfig("development", {})
      : defineConfig;

  start === "first" && console.log(logger.info("Creating configuration"));
  const config = merge(configFactory("development"), externalWebpacConfig);
  const devServer = merge(devServerConfig(), externalServer);

  const compiler = createCompiler(config, devServer, webpack);
  const server = new WebpackDevServer(devServer, compiler);

  // Launch WebpackDevServer.
  server.startCallback(() => {
    start === "first" &&
      console.log(logger.info("Starting the development server..."));
  });

  ["SIGINT", "SIGTERM"].forEach(function (sig) {
    process.on(sig, function () {
      server.stop();
      process.exit();
    });
  });

  process.stdin.on("end", function () {
    server.stop();
    process.exit();
  });
};

create();
