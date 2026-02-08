const path = require("path");

module.exports = {
  mode: "production",
  entry: "./es/index.js",
  output: {
    filename: "fetch-request.js",
    library: "fetch-request",
    libraryTarget: "umd",
    globalObject: "this",
    path: path.resolve(__dirname, "./dist"),
  },
  resolve: {
    extensions: [".json", ".js"],
  },
};
