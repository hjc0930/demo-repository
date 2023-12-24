import chalk from "./chalk.mjs";
import logger from "./logger.mjs";
import { appDist } from "./paths.mjs";

/**
 *
 * @param {UtilsType.Message[]} message
 * @param {"joinPathLength" | "sizeLength"} field
 * @returns {number}
 */
const getMaxFieldLength = (message, field) => {
  let maxLength = 0;
  message.forEach((item) => {
    if (item[field] > maxLength) {
      maxLength = item[field];
    }
  });
  return maxLength;
};

/**
 *
 * @param {UtilsType.Message[]} message
 */
const formatMessage = (message) => {
  const maxPathLength = getMaxFieldLength(message, "joinPathLength");
  const maxSizeLength = getMaxFieldLength(message, "sizeLength");

  message.sort((prev, curr) => prev.originSize - curr.originSize);
  message.forEach((item) => {
    let name =
      item.name + " ".repeat(maxPathLength - (item.path + item.name).length);
    let size = " ".repeat(maxSizeLength - item.size.length) + item.size;

    if (size.length < maxSizeLength) {
      size.padStart(maxSizeLength, "L");
    }
    console.log(
      " " +
        chalk.grey(item.path) +
        chalk.cyan(name) +
        "  " +
        (item.isSizeWarning ? chalk.yellow(size) : chalk.grey(size))
    );
  });
};

/**
 *
 * @param {Array} assets
 */
const printAssetsReport = (assets) => {
  const buildDirName = appDist.split("/").at(-1);
  const units = process.platform === "darwin" ? 1000 : 1024;
  console.log(logger.ready("Bundle infomation:\n"));
  const result = assets.map((asset) => {
    const formatItem = {
      name: asset.name.split("/").at(-1),
      path:
        [buildDirName, ...asset.name.split("/").slice(0, -1)].join("/") + "/",
      originSize: asset.size,
      isSizeWarning: asset.size / units >= 500,
      size:
        asset.size < units
          ? asset.size + " B"
          : (asset.size / units).toFixed(2) + " kB",
    };
    return {
      ...formatItem,
      joinPathLength: (formatItem.path + formatItem.name).length,
      sizeLength: formatItem.size.length,
    };
  });
  formatMessage(result);
  console.log();
  console.log(logger.success("Compiled successfully."));
};

export default printAssetsReport;
