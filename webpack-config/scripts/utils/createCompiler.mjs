import chalk from "./chalk.mjs";
import clearConsole from "./clearConsole.mjs";
import formatWebpackMessages from "./formatMessage.mjs";

const isInteractive = process.stdout.isTTY;

const printInstructions = () => {
  console.log();
  console.log(`You can now view React Application in the browser.`);
  console.log();
  console.log(`  Local:           http://localhost:3000`);
  console.log();
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
    console.log(chalk.red("Failed to compile."));
    console.log();
    // @ts-ignore
    console.log(err.message || err);
    console.log();
    process.exit(1);
  }

  compiler.hooks.invalid.tap("invalid", () => {
    if (isInteractive) {
      clearConsole();
    }
    console.log("Compiling...");
  });

  let isFirstCompile = true;

  compiler.hooks.done.tap("done", async (stats) => {
    if (isInteractive) {
      clearConsole();
    }

    const statsData = stats.toJson({
      all: false,
      warnings: true,
      errors: true,
    });

    const messages = formatWebpackMessages(statsData);
    const isSuccessful = !messages.errors.length && !messages.warnings.length;
    if (isSuccessful) {
      console.log(chalk.green("Compiled successfully!"));
    }
    if (isSuccessful && (isInteractive || isFirstCompile)) {
      printInstructions();
    }
    isFirstCompile = false;

    // If errors exist, only show errors.
    if (messages.errors.length) {
      // Only keep the first error. Others are often indicative
      // of the same problem, but confuse the reader with noise.
      if (messages.errors.length > 1) {
        messages.errors.length = 1;
      }
      console.log(chalk.red("Failed to compile.\n"));
      console.log(messages.errors.join("\n\n"));
      return;
    }

    // Show warnings if no errors were found.
    if (messages.warnings.length) {
      console.log(chalk.yellow("Compiled with warnings.\n"));
      console.log(messages.warnings.join("\n\n"));

      // Teach some ESLint tricks.
      console.log(
        "\nSearch for the " +
          chalk.underline(chalk.yellow("keywords")) +
          " to learn more about each warning."
      );
      console.log(
        "To ignore, add " +
          chalk.cyan("// eslint-disable-next-line") +
          " to the line before.\n"
      );
    }
  });
  return compiler;
};

export default createCompiler;
