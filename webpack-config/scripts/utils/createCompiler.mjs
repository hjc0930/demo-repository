import chalk from "./chalk.mjs";
import formatWebpackMessages from "./formatMessage.mjs";
import { networkInterfaces } from "node:os";
import logger from "./logger.mjs";

const isInteractive = process.stdout.isTTY;
let isFirstCompile = true;

/**
 * Get ip address
 * @returns {string[]}
 */
const getInternalIpAddress = () => {
  const interfaces = networkInterfaces();
  let networks = [];
  for (const devName in interfaces) {
    const iface = interfaces[devName];
    for (let i = 0; i < iface.length; i++) {
      const alias = iface[i];
      if (
        alias.family === "IPv4" &&
        alias.address !== "127.0.0.1" &&
        !alias.internal
      ) {
        networks.push(alias.address);
      }
    }
  }
  return networks;
};

const printInstructions = () => {
  const networks = getInternalIpAddress();
  console.log(
    logger.ready(
      `App listening at local: ${chalk.bright("http://localhost:3000")}`
    )
  );
  networks.forEach((item) => {
    console.log(
      logger.ready(
        `App listening at network: ${chalk.bright(`http://${item}:3000`)}`
      )
    );
  });
};

/**
 *
 * @param {import("webpack").Configuration} config
 * @param {import("webpack")} webpack
 * @returns
 */
const createCompiler = (config, webpack) => {
  let compiler;
  try {
    compiler = webpack(config);
  } catch (err) {
    console.log(logger.error("Failed to compile."));
    console.log();
    // @ts-ignore
    console.log(logger.error(err.message || err));
    console.log();
    process.exit(1);
  }

  compiler.hooks.invalid.tap("invalid", () => {
    console.log(logger.info("Preparing..."));
  });

  compiler.hooks.done.tap("done", async (stats) => {
    const statsData = stats.toJson({
      all: false,
      warnings: true,
      errors: true,
    });
    const messages = formatWebpackMessages(statsData);
    const isSuccessful = !messages.errors.length && !messages.warnings.length;
    if (isSuccessful && isInteractive) {
      const time = stats.endTime - stats.startTime;
      if (isFirstCompile) {
        printInstructions();
        console.log(logger.success(`Compiled successfully in ${time}ms`));
      } else {
        console.log(logger.success(`HMR completed, taking ${time}ms`));
      }
    }
    isFirstCompile = false;

    // If errors exist, only show errors.
    if (messages.errors.length) {
      // Only keep the first error. Others are often indicative
      // of the same problem, but confuse the reader with noise.
      if (messages.errors.length > 1) {
        messages.errors.length = 1;
      }
      console.log(logger.error("Failed to compile."));
      console.log(logger.error(messages.errors.join("\n\n")));
      return;
    }

    // Show warnings if no errors were found.
    if (messages.warnings.length) {
      console.log(logger.warning("Compiled with warnings.\n"));
      console.log(logger.warning(messages.warnings.join("\n\n")));
    }
  });
  return compiler;
};

export default createCompiler;
