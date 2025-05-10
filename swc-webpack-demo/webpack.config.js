const path = require("path");
const DotenvWebpack = require("dotenv-webpack");
const { SwcMinifyWebpackPlugin } = require("swc-minify-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");

const isProduction = process.env.NODE_ENV === "production";

/**
 * * @type {import('webpack').Configuration}
 */
module.exports = {
  mode: isProduction ? "production" : "development",
  devtool: isProduction ? "source-map" : "cheap-module-source-map",

  entry: path.resolve(__dirname, "src/index.tsx"),
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: "js/[name].[contenthash:8].js",
    chunkFilename: "js/[name].[contenthash:8].chunk.js",
    assetModuleFilename: "media/[name].[hash][ext]",
    publicPath: "/",
    clean: true,
  },

  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js", ".json"],
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },

  module: {
    rules: [
      {
        test: /\.(t|j)sx?$/,
        exclude: /node_modules/,
        use: {
          loader: "swc-loader",
          options: {
            jsc: {
              parser: {
                syntax: "typescript",
                tsx: true,
              },
              transform: {
                react: {
                  runtime: "automatic",
                  useBuiltins: true,
                },
              },
            },
            // 生产环境启用压缩
            minify: isProduction,
          },
        },
      },
      {
        test: /\.s[ac]ss$/i,
        use: [
          MiniCssExtractPlugin.loader,
          "css-loader",
          {
            loader: "sass-loader",
            options: {
              implementation: require("sass"),
            },
          },
        ],
      },
      {
        test: /\.svg$/i,
        issuer: /\.[jt]sx?$/,
        use: ["@svgr/webpack", "file-loader"],
      },
      {
        test: /\.(png|jpe?g|gif|woff2?|eot|ttf|otf)$/i,
        type: "asset/resource",
      },
    ],
  },

  plugins: [
    new MiniCssExtractPlugin({
      filename: "static/css/[name].[contenthash:8].css",
      chunkFilename: "static/css/[name].[contenthash:8].chunk.css",
    }),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, "public/index.html"),
      filename: "index.html",
      inject: true,
      minify: isProduction
        ? {
            removeComments: true,
            collapseWhitespace: true,
            removeAttributeQuotes: true,
          }
        : false,
    }),

    new DotenvWebpack({
      systemvars: true, // 加载系统环境变量
      safe: true, // 使用 .env.example 作为安全检查
      allowEmptyValues: false,
    }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, "public"),
          to: path.resolve(__dirname, "dist"),
          globOptions: {
            ignore: ["**/index.html"],
          },
        },
      ],
    }),
  ],

  optimization: {
    minimize: isProduction,
    minimizer: [
      new SwcMinifyWebpackPlugin({
        // 使用 SWC 压缩
        jsc: {
          minify: {
            compress: true,
            mangle: true,
          },
        },
      }),
    ],
    splitChunks: {
      chunks: "all",
      cacheGroups: {
        vendors: {
          test: /[\\/]node_modules[\\/]/,
          priority: -10,
          reuseExistingChunk: true,
        },
        default: {
          minChunks: 2,
          priority: -20,
          reuseExistingChunk: true,
        },
      },
    },
  },

  devServer: {
    hot: true,
    historyApiFallback: true,
    client: {
      logging: "error",
      overlay: {
        errors: true,
        warnings: false,
      },
    },
  },
};
