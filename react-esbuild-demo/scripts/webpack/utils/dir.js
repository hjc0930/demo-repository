const path = require("path");
const process = require("process");

const root = process.cwd();

module.exports = {
  isDev: process.env.NODE_ENV === "development",

  root,
  src: path.resolve(root, "./src"),
  dist: path.resolve(root, "./dist"),
  public: path.resolve(root, "./public"),
  nodeModules: path.resolve(root, "./node_modules"),
};
