import process from "node:process";
import configFactory from "./config/webpack.config.mjs";
import devServerConfig from "./config/webpackServer.config.mjs";
import createCompiler from "./utils/createCompiler.mjs";
import webpack from "webpack";
import WebpackDevServer from "webpack-dev-server";
import clearConsole from "./utils/clearConsole.mjs";
import chalk from "./utils/chalk.mjs";

process.env.BABEL_ENV = "development";
process.env.NODE_ENV = "development";

process.on("unhandledRejection", (err) => {
  throw err;
});

// Tools like Cloud9 rely on this.
const DEFAULT_PORT = parseInt(process.env.PORT, 10) || 3000;
const HOST = process.env.HOST || "0.0.0.0";
const isInteractive = process.stdout.isTTY;

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
  if (isInteractive) {
    clearConsole();
  }
  console.log(chalk.cyan("Starting the development server...\n"));
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
