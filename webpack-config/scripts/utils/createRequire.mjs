import { createRequire as nodeCreateRequire } from "node:module";

/**
 *
 * @param {string} metaUrl
 * @returns {NodeRequire}
 */
const createRequire = (metaUrl) => nodeCreateRequire(metaUrl);

export default createRequire;
