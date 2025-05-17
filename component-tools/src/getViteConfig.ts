import { InlineConfig } from "vite";
import fs from "fs-extra";
import react from "@vitejs/plugin-react";
import process from "node:process";
import { getProjectPath } from "./utils/projectHelper";

const packageJson = fs.readJsonSync(getProjectPath("package.json"));

const getConfig = async (): Promise<InlineConfig> => {
  const res = Object.keys(process.env).reduce<Record<string, any>>(
    (acc, key) => {
      if (key.startsWith("VITE_")) {
        acc[key] = process.env[key];
      }
      return acc;
    },
    {}
  );

  const config: InlineConfig = {
    plugins: [react()],
    define: {
      "process.env": {
        ...res,
        NODE_ENV: JSON.stringify("production"),
      },
    },

    build: {
      copyPublicDir: false,
      outDir: "dist",
      lib: {
        entry: "./index.js",
        name: packageJson.name,
        fileName: () => `${packageJson.name}.min.js`,
        cssFileName: `${packageJson.name}.min`,
        formats: ["umd"],
      },
      rollupOptions: {
        output: {
          globals: {
            react: "React",
            "react-dom": "ReactDOM",
          },
        },
      },
    },
  };

  return config;
};

export default getConfig;
