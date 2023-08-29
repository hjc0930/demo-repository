const path = require("path");
const { dir, plugins, loaders } = require("./utils");

module.exports = {
  entry: {
    index: path.resolve(dir.src, "./index"),
  },

  output: {
    path: dir.dist,
    filename: `js/[name]${dir.isDev ? "" : "-[contenthash:8]"}.js`,
    clean: true,
  },
  watchOptions: {
    ignored: /node_modules|\.babelrc/,
  },
  resolve: {
    alias: {
      "@": dir.src,
      noParseReact: path.resolve(
        dir.nodeModules,
        dir.isDev
          ? "./react/cjs/react.development.js"
          : "./react/cjs/react.production.js"
      ),
    },
    extensions: [".tsx", ".ts", ".jsx", ".js"],
  },
  module: {
    noParse: /noParseReact/,
    rules: loaders,
  },

  plugins,
};
