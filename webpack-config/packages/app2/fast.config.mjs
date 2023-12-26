import { defineConfig } from "fast-cli";
import { fileURLToPath, URL } from "node:url";
import webpack from "webpack";
import MySingleReactRefreshPlugin from "single-react-refresh-plugin";

const { ModuleFederationPlugin } = webpack.container;

export default defineConfig(() => {
  return {
    output: {
      publicPath: "http://localhost:3001/",
    },
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
    server: {
      host: "0.0.0.0",
      port: 3001,
    },
    // optimization: {
    //   runtimeChunk: "single",
    // },
    plugins: [
      new ModuleFederationPlugin({
        name: "app2",
        filename: "remoteEntry.js",
        exposes: {
          "./App": "./src/App.tsx",
        },
        shared: {
          react: { singleton: false },
          "react-dom": { singleton: true },
        },
      }),
      new MySingleReactRefreshPlugin(),
    ],
  };
});
