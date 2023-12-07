import configFactory from "./config/webpack.config.mjs";
import webpack from "webpack";
import process from "node:process";
import formatWebpackMessages from "./utils/formatMessage.mjs";
import printAssetsReport from "./utils/printAssetsReport.mjs";
import chalk from "./utils/chalk.mjs";
import logger from "./utils/logger.mjs";

process.env.BABEL_ENV = "production";
process.env.NODE_ENV = "production";

process.on("unhandledRejection", (err) => {
  throw err;
});

console.log(logger.info(chalk.bright(chalk.cyan("UIKIT CLI"))));
const config = configFactory("production");

function build() {
  console.log(logger.info("Creating an optimized production build..."));

  return new Promise((resolve) => {
    const compiler = webpack(config);

    compiler.run((err, stats) => {
      /**
       * @type {Build.Message}
       */
      let messages;

      if (err) {
        if (!err.message) {
          throw err;
        }

        let errMessage = err.message;

        if (Object.prototype.hasOwnProperty.call(err, "postcssNode")) {
          errMessage +=
            "\nCompileError: Begins at CSS selector " +
            err["postcssNode"].selector;
        }

        messages = formatWebpackMessages({
          errors: [errMessage],
          warnings: [],
        });
      } else {
        messages = formatWebpackMessages(
          stats.toJson({ all: false, warnings: true, errors: true })
        );
      }

      if (messages.errors.length) {
        if (messages.errors.length > 1) {
          messages.errors.length = 1;
        }
        throw new Error(messages.errors.join("\n\n"));
      }
      resolve(stats);
    });
  });
}

build().then((stats) => {
  printAssetsReport(stats?.toJson()?.assets ?? []);
});
