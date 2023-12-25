#!/usr/bin/env node
import process from "node:process";
import { createInterface } from "node:readline";

import chokidar from "chokidar";
import { spawn } from "child_process";
import { createRequire } from "node:module";
import path from "node:path";
import logger from "../scripts/utils/logger.mjs";
import chalk from "../scripts/utils/chalk.mjs";

process.env.START = "first";

process.on("unhandledRejection", (err) => {
  throw err;
});

const require = createRequire(import.meta.url);
const argv = process.argv.slice(2);
let complier = null;
/**
 *
 * @param {Function} reStart
 */
const watchFn = (reStart) => {
  const watch = chokidar
    .watch(path.resolve(process.cwd(), "./fast.config.mjs"))
    .on("change", () => {
      process.env.START = "restart";
      console.log(
        logger.info(
          chalk.green("fast.config.mjs changed, restarting server...")
        )
      );
      watch.close();
      reStart();
    });
};

const watchLine = () => {
  const rl = createInterface({
    input: process.stdin,
    output: process.stdout,
  });
  rl.on("line", (line) => {
    switch (line) {
      case "q":
        complier.kill();
        rl.close();
        process.exit(0);
    }
  });
};

const start = () => {
  complier = spawn("node", [require.resolve("../scripts/start.mjs")], {
    stdio: [process.stdin, process.stdout, process.stderr],
  });

  watchFn(() => {
    complier.kill();
    complier = null;
    start();
  });
  watchLine();
};

if (argv.includes("start")) {
  start();
}
