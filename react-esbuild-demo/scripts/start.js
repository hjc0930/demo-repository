process.env.NODE_ENV = "development";

const webpack = require("webpack");
const WebpackServer = require("webpack-dev-server");
const webpackDevConfig = require("./webpack/webpack.dev");

// webpack compiler
const compiler = webpack(webpackDevConfig);
// webpack server
const server = new WebpackServer(webpackDevConfig.devServer, compiler);

(async () => {
  await server.start();
})();
