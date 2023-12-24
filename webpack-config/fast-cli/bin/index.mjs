#!/usr/bin/env node
import process from "node:process";
import { spawnSync } from "child_process";
import { createRequire } from "node:module";
const require = createRequire(import.meta.url);

const argv = process.argv.slice(2);

if (argv.includes("start")) {
  spawnSync("node", [require.resolve("../scripts/start.mjs")], {
    stdio: "inherit",
  });
}
