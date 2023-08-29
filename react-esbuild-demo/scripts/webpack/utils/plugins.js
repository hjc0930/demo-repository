const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const { ProgressPlugin, DefinePlugin } = require("webpack");
const path = require("path");
const stringEnv = require("./env");

const { root, isDev, public, dist } = require("./dir");

module.exports = [
  new HtmlWebpackPlugin({
    template: path.resolve(root, "./index.html"),
    filename: "index.html",
    cache: false,
    minify: isDev
      ? false
      : {
          removeAttributeQuotes: true,
          collapseWhitespace: true,
          removeComments: true,
          collapseBooleanAttributes: true,
          collapseInlineTagWhitespace: true,
          removeRedundantAttributes: true,
          removeScriptTypeAttributes: true,
          removeStyleLinkTypeAttributes: true,
          minifyCSS: true,
          minifyJS: true,
          minifyURLs: true,
          useShortDoctype: true,
        },
  }),
  new CopyWebpackPlugin({
    patterns: [
      {
        from: public,
        to: dist,
        toType: "dir",
        globOptions: {
          dot: true,
          gitignore: true,
          ignore: ["**/*.html"],
        },
      },
    ],
  }),
  new DefinePlugin(stringEnv),
  new ProgressPlugin(),
];
