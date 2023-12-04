import { appDist } from "./paths.mjs";

/**
 *
 * @param {Array} assets
 */
const printAssetsReport = (assets) => {
  const buildDirName = appDist.split("/").at(-1);
  const units = process.platform === "darwin" ? 1000 : 1024;
  console.log();
  console.log("File sizes:\n");
  assets.forEach((asset) => {
    const formatAssets = {
      name: asset.name,
      path: `${buildDirName}/${asset.name}`,
      originSize: asset.size,
      size:
        asset.size < units
          ? asset.size + " B"
          : (asset.size / units).toFixed(2) + " kB",
    };
    console.log(formatAssets.size + "  " + formatAssets.path);
  });
  console.log();
};

export default printAssetsReport;
