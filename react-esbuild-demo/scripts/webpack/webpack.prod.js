const { merge } = require("webpack-merge");
const base = require("./webpack.base");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");
const TerserPlugin = require("terser-webpack-plugin");

module.exports = merge(base, {
  mode: "production",
  devtool: false,

  stats: {
    assets: true,
    assetsSort: "!size",
    modules: false,
  },

  optimization: {
    minimize: true,
    usedExports: true,
    minimizer: [
      new TerserPlugin({
        parallel: true,
        exclude: /node_modules/,
        extractComments: false, //不将注释提取到单独的文件中
        terserOptions: {
          compress: { pure_funcs: ["console.log", "debugger"] }, // 生产去除console.log
        },
      }),
      new CssMinimizerPlugin(),
    ],
  },
  performance: {
    // 设置所有产物体积阈值
    maxAssetSize: 172 * 1024,
    // 设置 entry 产物体积阈值
    maxEntrypointSize: 244 * 1024,
    // 报错方式，支持 `error` | `warning` | false
    hints: "warning",
    // 过滤需要监控的文件类型
    assetFilter: function (assetFilename) {
      return assetFilename.endsWith(".js");
    },
  },

  module: {
    rules: [
      {
        test: /\.(j|t)sx?$/i,
        exclude: /node_modules/,
        loader: "babel-loader",
        options: {
          cacheDirectory: true,
        },
      },
    ],
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: "css/[name]-[contenthash:8].css",
    }),
  ],
});
