import { Compiler } from "webpack";
import { resolveApp } from "./utils/path";


class WatchFilePlugin {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor() { }

  apply(compiler: Compiler) {
    compiler.hooks.done.tapAsync("WatchFilePlugin", (stats, callback) => {
      stats.compilation.fileDependencies.add(resolveApp("./.configrc.ts"));
      callback();
    });
  }
}

export default WatchFilePlugin;
