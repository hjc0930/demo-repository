// import process from "node:process";
// import { createRequire } from "node:module";
// import { readdirSync, existsSync, statSync } from "node:fs";
// import path from "node:path";

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
// const require = createRequire(import.meta.url);

// // /Users/huangjiacheng/workspace/demo-repository/webpack-config/node_modules/babel-preset-react-app/dependencies.js
// console.log(require.resolve("babel-preset-react-app/dependencies"));

// // Users/huangjiacheng/workspace/demo-repository/webpack-config/node_modules/babel-preset-react-app/dependencies.js
// console.log(require.resolve("babel-preset-react-app/dependencies"));

// function printDistReport(dirPath) {
//   if (!existsSync(dirPath)) return;
//   const dirent = readdirSync(dirPath, { withFileTypes: true });
//   if (!dirent.length) return;

//   const result = dirent.map((info) => {
//     if (!info.isDirectory()) {
//       return {
//         name: info.name,
//         path: `${info.path}/${info.name}`,
//         size: `${statSync(info.path).size}kb`,
//       };
//     }
//     return printDistReport(`${info.path}/${info.name}`);
//   });

//   return result;
// }

// const appDist = path.resolve(process.cwd(), "./dist");

// console.log(printDistReport(appDist));

// console.log("\x1B[31m%s\x1B[0m", "红色");
// console.log("3333");
// console.log("\x1B[32m%s\x1B[0m", "红色");

import { networkInterfaces } from "node:os";

const interfaces = networkInterfaces();

let ipaddressArr = [];
for (const devName in interfaces) {
  const iface = interfaces[devName];
  for (let i = 0; i < iface.length; i++) {
    const alias = iface[i];
    if (
      alias.family === "IPv4" &&
      alias.address !== "127.0.0.1" &&
      !alias.internal
    ) {
      ipaddressArr.push(alias.address);
    }
  }
}

console.log(ipaddressArr);
