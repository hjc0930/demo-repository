import configFactory from "./config/webpack.config.mjs";
import webpack from "webpack";
import process from "node:process";

process.on("unhandledRejection", (err) => {
  throw err;
});

const config = configFactory("production");

function build() {
  console.log("Creating an optimized production build...");

  const compiler = webpack(config);

  compiler.run((err, stats) => {
    /** @type {Build.Message} */
    let messages;

    if (err) {
      if (!err.message) {
        throw err;
      }

      let errMessage = err.message;

      // Add additional information for postcss errors
      if (Object.prototype.hasOwnProperty.call(err, "postcssNode")) {
        errMessage +=
          "\nCompileError: Begins at CSS selector " +
          err["postcssNode"].selector;
      }

      messages = {
        errors: [errMessage],
        warnings: [],
      };
    } else {
      messages = stats.toJson({ all: false, warnings: true, errors: true });
    }

    if (messages.errors.length) {
      if (messages.errors.length > 1) {
        messages.errors.length = 1;
      }
      throw new Error(messages.errors.join("\n\n"));
    }
    console.log("Compiled successfully.");
  });
}

build();
