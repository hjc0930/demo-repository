import { defineConfig } from "fast-cli";
import { fileURLToPath, URL } from "node:url";
import webpack from "webpack";
import MySingleReactRefreshPlugin from "single-react-refresh-plugin";

const { ModuleFederationPlugin } = webpack.container;

export default defineConfig(() => {
  return {
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
    server: {
      host: "0.0.0.0",
      port: 3000,
    },
    // optimization: {
    //   runtimeChunk: "single",
    // },
    plugins: [
      new ModuleFederationPlugin({
        name: "app1",
        filename: "remoteEntry.js",
        remotes: {
          app2: "app2@http://localhost:3001/remoteEntry.js",
        },
        shared: {
          react: { singleton: false, requiredVersion: "18.2.0" },
          "react-dom": { singleton: true },
        },
      }),
      new MySingleReactRefreshPlugin(),
    ],
  };
});
