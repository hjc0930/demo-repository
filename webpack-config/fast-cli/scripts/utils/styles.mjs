import MiniCssExtractPlugin from "mini-css-extract-plugin";
import { appSrc } from "./paths.mjs";
import createRequire from "./createRequire.mjs";

const require = createRequire(import.meta.url);
// style files regexes
const cssRegex = /\.css$/;
const cssModuleRegex = /\.module\.css$/;
const sassRegex = /\.(scss|sass)$/;
const sassModuleRegex = /\.module\.(scss|sass)$/;

/**
 * common function to get style loaders
 * @param {boolean} isEnvProduction
 * @param {Record<string, any>} cssOptions
 * @param {string} [preProcessor]
 * @returns
 */
const getStyleLoaders = (isEnvProduction, cssOptions, preProcessor) => {
  const loaders = [
    !isEnvProduction && require.resolve("style-loader"),
    isEnvProduction && {
      loader: MiniCssExtractPlugin.loader,
    },
    {
      loader: require.resolve("css-loader"),
      options: cssOptions,
    },
    {
      // Options for PostCSS as we reference these options twice
      // Adds vendor prefixing based on your specified browser support in
      // package.json
      loader: require.resolve("postcss-loader"),
      options: {
        postcssOptions: {
          // Necessary for external CSS imports to work
          // https://github.com/facebook/create-react-app/issues/2677
          ident: "postcss",
          config: false,
          plugins: [
            "postcss-flexbugs-fixes",
            [
              "postcss-preset-env",
              {
                autoprefixer: {
                  flexbox: "no-2009",
                },
                stage: 3,
              },
            ],
            // Adds PostCSS Normalize as the reset css with default options,
            // so that it honors browserslist config in package.json
            // which in turn let's users customize the target behavior as per their needs.
            "postcss-normalize",
          ],
        },
        sourceMap: false,
      },
    },
  ].filter(Boolean);
  if (preProcessor) {
    loaders.push(
      {
        loader: require.resolve("resolve-url-loader"),
        options: {
          sourceMap: false,
          root: appSrc,
        },
      },
      {
        loader: require.resolve(preProcessor),
        options: {
          sourceMap: true,
        },
      }
    );
  }
  return loaders;
};

export {
  cssRegex,
  cssModuleRegex,
  sassRegex,
  sassModuleRegex,
  getStyleLoaders,
};
