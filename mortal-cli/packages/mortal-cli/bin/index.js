#! /usr/bin/env node
import parseArgv from "./utils/parseArgv.js";

const buildResult = parseArgv("build", ["mode"]);

if (buildResult[0] === "build") {
  console.log(buildResult);
} else {
}
