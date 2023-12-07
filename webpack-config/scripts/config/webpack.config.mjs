import process from "node:process";
import { fileURLToPath } from "node:url";
import {
  appDist,
  appEntry,
  appHtml,
  appNodeModules,
  appPackageJson,
  appPath,
  appPublic,
  appSrc,
  appWebpackCatch,
  moduleFileExtensions,
} from "../utils/paths.mjs";
import TerserPlugin from "terser-webpack-plugin";
import CopyWebpackPlugin from "copy-webpack-plugin";
import HtmlWebpackPlugin from "html-webpack-plugin";
import createRequire from "../utils/createRequire.mjs";
import MiniCssExtractPlugin from "mini-css-extract-plugin";
import CssMinimizerPlugin from "css-minimizer-webpack-plugin";
import createEnvironmentHash from "../utils/createEnvironmentHash.mjs";
import getCSSModuleLocalIdent from "../utils/getCSSModuleLocalIdent.mjs";
import ReactRefreshWebpackPlugin from "@pmmmwh/react-refresh-webpack-plugin";
import CaseSensitivePathsPlugin from "case-sensitive-paths-webpack-plugin";
import ModuleNotFoundPlugin from "../utils/ModuleNotFuntPlugin.mjs";
import {
  cssModuleRegex,
  cssRegex,
  getStyleLoaders,
  sassModuleRegex,
  sassRegex,
} from "../utils/styles.mjs";
import ModuleScopePlugin from "../utils/ModuleScopePlugin.mjs";
import logger from "../utils/logger.mjs";

const require = createRequire(import.meta.url);

const reactRefreshRuntimeEntry = require.resolve("react-refresh/runtime");
const reactRefreshWebpackPluginRuntimeEntry = require.resolve(
  "@pmmmwh/react-refresh-webpack-plugin"
);
const babelRuntimeEntry = require.resolve("babel-preset-react-app");
const babelRuntimeEntryHelpers = require.resolve(
  "@babel/runtime/helpers/esm/assertThisInitialized",
  { paths: [babelRuntimeEntry] }
);
const babelRuntimeRegenerator = require.resolve("@babel/runtime/regenerator", {
  paths: [babelRuntimeEntry],
});

const imageInlineSizeLimit = 10000;

/**
 *
 * @param {"development" | "production"} mode
 * @returns {import("webpack").Configuration}
 */
const configFactory = (mode) => {
  console.log(logger.info("Creating configuration"));
  const isEnvProduction = mode === "production";

  return {
    mode,
    stats: "none",
    entry: appEntry,
    bail: isEnvProduction,
    devtool: isEnvProduction ? false : "cheap-module-source-map",
    output: {
      clean: true,
      path: appDist,
      filename: isEnvProduction
        ? "assets/[name].[contenthash:8].js"
        : "assets/bundle.js",
      chunkFilename: isEnvProduction
        ? "assets/[name].[contenthash:8].chunk.js"
        : "assets/[name].chunk.js",
      assetModuleFilename: "assets/[name].[hash][ext]",
    },
    cache: {
      type: "filesystem",
      store: "pack",
      // TODO: 获取环境变量
      version: createEnvironmentHash(process.env),
      cacheDirectory: appWebpackCatch,
      buildDependencies: {
        defaultWebpack: ["webpack/lib/"],
        config: [fileURLToPath(import.meta.url)],
      },
    },
    /** 关闭 webpack-dev-server的初始化输出 */
    infrastructureLogging: {
      level: "none",
    },
    optimization: {
      minimize: isEnvProduction,
      minimizer: [
        // This is only used in production mode
        new TerserPlugin({
          extractComments: false,
          terserOptions: {
            parse: {
              // We want terser to parse ecma 8 code. However, we don't want it
              // to apply any minification steps that turns valid ecma 5 code
              // into invalid ecma 5 code. This is why the 'compress' and 'output'
              // sections only apply transformations that are ecma 5 safe
              // https://github.com/facebook/create-react-app/pull/4234
              // @ts-ignore
              ecma: 8,
            },
            compress: {
              ecma: 5,
              // @ts-ignore
              warnings: false,
              comparisons: false,
              inline: 2,
            },
            mangle: {
              safari10: true,
            },
            // keep_classnames: false,
            // keep_fnames: false,
            output: {
              ecma: 5,
              comments: false,
              ascii_only: true,
            },
          },
        }),
        // This is only used in production mode
        new CssMinimizerPlugin(),
      ],
    },
    resolve: {
      modules: ["node_modules", appNodeModules],
      extensions: moduleFileExtensions.map((ext) => `.${ext}`),
      plugins: [
        new ModuleScopePlugin(appSrc, [
          appPackageJson,
          reactRefreshRuntimeEntry,
          reactRefreshWebpackPluginRuntimeEntry,
          babelRuntimeEntry,
          babelRuntimeEntryHelpers,
          babelRuntimeRegenerator,
        ]),
      ],
    },
    module: {
      strictExportPresence: true,
      rules: [
        {
          oneOf: [
            {
              test: [/\.avif$/],
              type: "asset",
              mimetype: "image/avif",
              parser: {
                dataUrlCondition: {
                  maxSize: imageInlineSizeLimit,
                },
              },
            },
            {
              test: [/\.bmp$/, /\.gif$/, /\.jpe?g$/, /\.png$/],
              type: "asset",
              parser: {
                dataUrlCondition: {
                  maxSize: imageInlineSizeLimit,
                },
              },
            },
            {
              test: /\.svg$/,
              use: [
                {
                  loader: require.resolve("@svgr/webpack"),
                  options: {
                    prettier: false,
                    svgo: false,
                    svgoConfig: {
                      plugins: [{ removeViewBox: false }],
                    },
                    titleProp: true,
                    ref: true,
                  },
                },
                {
                  loader: require.resolve("file-loader"),
                  options: {
                    name: "assets/[name].[hash].[ext]",
                  },
                },
              ],
              issuer: {
                and: [/\.(ts|tsx|js|jsx|md|mdx)$/],
              },
            },
            // Process application JS with Babel.
            // The preset includes JSX, Flow, TypeScript, and some ESnext features.
            {
              test: /\.(js|mjs|jsx|ts|tsx)$/,
              include: appSrc,
              loader: require.resolve("babel-loader"),
              options: {
                customize: require.resolve(
                  "babel-preset-react-app/webpack-overrides"
                ),
                presets: [
                  [
                    require.resolve("babel-preset-react-app"),
                    {
                      runtime: "automatic",
                    },
                  ],
                ],

                plugins: [
                  !isEnvProduction && require.resolve("react-refresh/babel"),
                ].filter(Boolean),
                // This is a feature of `babel-loader` for webpack (not Babel itself).
                // It enables caching results in ./node_modules/.cache/babel-loader/
                // directory for faster rebuilds.
                cacheDirectory: true,
                cacheCompression: false,
                compact: isEnvProduction,
              },
            },
            /**
             * Process any JS outside of the app with Babel.
             * Unlike the application JS, we only compile the standard ES features.
             */
            {
              test: /\.(js|mjs)$/,
              exclude: /@babel(?:\/|\\{1,2})runtime/,
              loader: require.resolve("babel-loader"),
              options: {
                babelrc: false,
                configFile: false,
                compact: false,
                presets: [
                  [
                    require.resolve("babel-preset-react-app/dependencies"),
                    { helpers: true },
                  ],
                ],
                cacheDirectory: true,
                cacheCompression: false,
                sourceMaps: false,
                inputSourceMap: false,
              },
            },
            // "postcss" loader applies autoprefixer to our CSS.
            // "css" loader resolves paths in CSS and adds assets as dependencies.
            // "style" loader turns CSS into JS modules that inject <style> tags.
            // In production, we use MiniCSSExtractPlugin to extract that CSS
            // to a file, but in development "style" loader enables hot editing
            // of CSS.
            // By default we support CSS Modules with the extension .module.css
            {
              test: cssRegex,
              exclude: cssModuleRegex,
              use: getStyleLoaders(isEnvProduction, {
                importLoaders: 1,
                sourceMap: false,
                modules: {
                  mode: "icss",
                },
              }),
              // Don't consider CSS imports dead code even if the
              // containing package claims to have no side effects.
              // Remove this when webpack adds a warning or an error for this.
              // See https://github.com/webpack/webpack/issues/6571
              sideEffects: true,
            },
            // Adds support for CSS Modules (https://github.com/css-modules/css-modules)
            // using the extension .module.css
            {
              test: cssModuleRegex,
              use: getStyleLoaders(isEnvProduction, {
                importLoaders: 1,
                sourceMap: false,
                modules: {
                  mode: "local",
                  getLocalIdent: getCSSModuleLocalIdent,
                },
              }),
            },
            // Opt-in support for SASS (using .scss or .sass extensions).
            // By default we support SASS Modules with the
            // extensions .module.scss or .module.sass
            {
              test: sassRegex,
              exclude: sassModuleRegex,
              use: getStyleLoaders(
                isEnvProduction,
                {
                  importLoaders: 3,
                  sourceMap: false,
                  modules: {
                    mode: "icss",
                  },
                },
                "sass-loader"
              ),
              // Don't consider CSS imports dead code even if the
              // containing package claims to have no side effects.
              // Remove this when webpack adds a warning or an error for this.
              // See https://github.com/webpack/webpack/issues/6571
              sideEffects: true,
            },
            // Adds support for CSS Modules, but using SASS
            // using the extension .module.scss or .module.sass
            {
              test: sassModuleRegex,
              use: getStyleLoaders(
                isEnvProduction,
                {
                  importLoaders: 3,
                  sourceMap: false,
                  modules: {
                    mode: "local",
                    getLocalIdent: getCSSModuleLocalIdent,
                  },
                },
                "sass-loader"
              ),
            },
            {
              // Exclude `js` files to keep "css" loader working as it injects
              // its runtime that would otherwise be processed through "file" loader.
              // Also exclude `html` and `json` extensions so they get processed
              // by webpacks internal loaders.
              exclude: [/^$/, /\.(js|mjs|jsx|ts|tsx)$/, /\.html$/, /\.json$/],
              type: "asset/resource",
            },
          ],
        },
      ].filter(Boolean),
    },
    plugins: [
      new HtmlWebpackPlugin(
        Object.assign(
          {},
          {
            inject: true,
            template: appHtml,
          },
          isEnvProduction
            ? {
                minify: {
                  removeComments: true,
                  collapseWhitespace: true,
                  removeRedundantAttributes: true,
                  useShortDoctype: true,
                  removeEmptyAttributes: true,
                  removeStyleLinkTypeAttributes: true,
                  keepClosingSlash: true,
                  minifyJS: true,
                  minifyCSS: true,
                  minifyURLs: true,
                },
              }
            : undefined
        )
      ),
      !isEnvProduction &&
        new ReactRefreshWebpackPlugin({
          overlay: false,
        }),
      /**
       * Watcher doesn't work well if you mistype casing in a path so we use
       * a plugin that prints an error when you attempt to do this.
       */
      !isEnvProduction && new CaseSensitivePathsPlugin(),
      isEnvProduction &&
        new MiniCssExtractPlugin({
          filename: "assets/[name].[contenthash:8].css",
          chunkFilename: "assets/[name].[contenthash:8].chunk.css",
        }),
      new CopyWebpackPlugin({
        patterns: [
          {
            from: appPublic,
            to: appDist,
            filter: (item) => {
              return !item.includes(".html");
            },
          },
        ],
      }),
      new ModuleNotFoundPlugin(appPath),
    ].filter(Boolean),
    performance: false,
  };
};

export default configFactory;
