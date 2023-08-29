const { merge } = require("webpack-merge");
const base = require("./webpack.base");
const FriendlyErrorsWebpackPlugin = require("friendly-errors-webpack-plugin");
const ReactRefreshPlugin = require("@pmmmwh/react-refresh-webpack-plugin");
const ip = require("ip");
const { utils } = require("./utils");

const PORT = 8100;

module.exports = merge(base, {
  mode: "development",
  devtool: "cheap-module-source-map",
  stats: "errors-only",

  cache: {
    type: "filesystem",
  },
  devServer: {
    host: "0.0.0.0",
    port: PORT,
    hot: true,
    compress: true,
    client: {
      logging: "error",
    },
    historyApiFallback: true,
  },

  optimization: {
    removeAvailableModules: false,
    removeEmptyChunks: false,
    splitChunks: false,
    minimize: false,
    concatenateModules: false,
    usedExports: false,
  },
  module: {
    rules: [
      {
        test: /\.(j|t)sx?$/i,
        exclude: /node_modules/,
        loader: 'esbuild-loader',
        options: {
          loader: 'tsx',
          target: 'es2015',
          jsx: 'automatic',
        },
        // loader: "babel-loader",
        // options: {
        //   cacheDirectory: true,
        //   plugins: ["react-refresh/babel"],
        // },
      },
    ],
  },
  plugins: [
    new FriendlyErrorsWebpackPlugin({
      compilationSuccessInfo: {
        messages: [
          `\r App running at:\n - Local：  ${utils.printColor(
            `http://localhost:${PORT}`,
            "blue"
          )}\n - Network: ${utils.printColor(
            `http://${ip.address()}:${PORT}`,
            "blue"
          )}`,
        ],
      },
      clearConsole: true,
    }),
    new ReactRefreshPlugin(),
  ],
});
