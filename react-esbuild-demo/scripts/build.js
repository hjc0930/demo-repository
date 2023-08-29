process.env.NODE_ENV = "production";

const webpack = require("webpack");
const webpackProdConfig = require("./webpack/webpack.prod");

// webpack compiler
const compiler = webpack(webpackProdConfig);

console.log("Build Start...");
compiler.run(() => {
  console.log("Build Success");
});
