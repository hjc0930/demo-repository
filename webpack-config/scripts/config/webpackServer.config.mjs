import { appPublic } from "../utils/paths.mjs";

/**
 * @returns {import("webpack-dev-server").Configuration}
 */
const devServer = () => {
  return {
    allowedHosts: "all",
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "*",
      "Access-Control-Allow-Headers": "*",
    },
    compress: true,
    hot: true,
    static: {
      directory: appPublic,
    },
    client: {
      overlay: {
        errors: true,
        warnings: false,
      },
    },
    // devMiddleware: {
    //   publicPath: paths.publicUrlOrPath.slice(0, -1),
    // },
    historyApiFallback: true,
    // historyApiFallback: {
    //   disableDotRule: true,
    //   index: paths.publicUrlOrPath,
    // },
  };
};

export default devServer;
