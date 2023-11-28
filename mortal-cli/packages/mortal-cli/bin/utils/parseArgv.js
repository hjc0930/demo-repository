import { argv as _argv } from "node:process";

/**
 *
 * @param {string} params
 * @param {...string} args
 * @returns {any[]}
 */
function parseArgv(params, ...args) {
  /** @type {Array<string>} */
  const argv = _argv;
  const result = [];

  const index = argv.indexOf(params);
  if (index === -1) {
    return [];
  }
  const execArr = argv.slice(index);
  result[0] = execArr[0];

  if (args && args.length) {
    const restExecArr = execArr.slice(1);
    result[1] = {};
    args.forEach((arg) => {
      const argStr = restExecArr.find((item) => item.includes(arg));
      if (argStr) {
        result[1][arg] = argStr.split("=")[1];
      }
    });
  }

  return result;
}

export default parseArgv;
