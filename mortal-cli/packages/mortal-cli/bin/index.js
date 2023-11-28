#! /usr/bin/env node
const process = require("node:process");

console.log(process.argv);
// const yargs = require("yargs");
// const { hideBin } = require("yargs/helpers");

// yargs(hideBin(process.argv))
//   .command(
//     ["build"],
//     "This is a build test",
//     function (yargs) {
//       return yargs.option("mode", {
//         describe: "Running mode",
//         type: "string",
//       });
//     },
//     (argv) => {
//       console.log(argv.mode);
//     }
//   )
//   .parse();
