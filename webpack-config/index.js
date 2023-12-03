// import process from "node:process";
import { createRequire } from "node:module";

// function checkNodeVersion() {
//   // 12.22.12
//   const version = process.version.slice(1);
//   const [major, minor, patch] = version
//     .split(".")
//     .map((item) => parseInt(item, 10));
//   console.log({ major, minor, patch });
// }

// checkNodeVersion();

// /**
//  * @type {import("webpack").Configuration}
//  */
// const config = {};
const require = createRequire(import.meta.url);

// /Users/huangjiacheng/workspace/demo-repository/webpack-config/node_modules/babel-preset-react-app/dependencies.js
console.log(require.resolve("babel-preset-react-app/dependencies"));

// Users/huangjiacheng/workspace/demo-repository/webpack-config/node_modules/babel-preset-react-app/dependencies.js
console.log(require.resolve("babel-preset-react-app/dependencies"));
