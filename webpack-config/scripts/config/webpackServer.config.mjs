/**
 * @returns {import("webpack-dev-server").Configuration}
 */
const devServer = () => {
  return {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "*",
      "Access-Control-Allow-Headers": "*",
    },
    hot: true,
    compress: true,
  };
};

export default devServer;
