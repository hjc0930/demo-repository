const path = require("path");

module.exports = {
  mode: "production",
  entry: "./es/index.js",
  output: {
    filename: "types-asserts.js",
    library: "types-asserts",
    libraryTarget: "umd",
    globalObject: "this",
    path: path.resolve(__dirname, "./dist"),
  },
  resolve: {
    extensions: [".json", ".js"],
  },
};
