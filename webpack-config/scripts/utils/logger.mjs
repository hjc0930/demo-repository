import chalk from "./chalk.mjs";

const chalkMap = {
  info: chalk.cyan,
  ready: chalk.blue,
  success: chalk.green,
  warning: chalk.yellow,
  error: chalk.red,
};

/**
 * @param {"info" | "success" | "warning" | "error"} type
 * @param {string} message
 * @param {string} [typeMessage]
 */
const logger = (type, message, typeMessage) => {
  return `${chalkMap[type](typeMessage ?? type)} - ${message}`;
};

logger.info = (message) => `${chalkMap.info("info   ")} - ${message}`;
logger.ready = (message) => `${chalkMap.ready("ready  ")} - ${message}`;
logger.success = (message) => `${chalkMap.success("success")} - ${message}`;
logger.warning = (message) => `${chalkMap.warning("warning")} - ${message}`;
logger.error = (message) => `${chalkMap.error("error  ")} - ${message}`;

export default logger;
