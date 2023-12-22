import defineConfig from "./defineConfig.mjs";

export default defineConfig((mode, env) => {
  return {
    publicPath: "",
    server: {
      port: 3000,
      host: "0.0.0.0",
    },
  };
});
