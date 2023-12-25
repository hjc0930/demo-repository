import chalk from "./chalk.mjs";

const chalkMap = {
  info: chalk.cyan,
  ready: chalk.blue,
  success: chalk.green,
  warning: chalk.yellow,
  error: chalk.red,
};

const logger = {};

logger.info = (message) => `${chalkMap.info("info ")} - ${message}`;
logger.ready = (message) => `${chalkMap.ready("ready")} - ${message}`;
logger.success = (message) => `${chalkMap.success("succ ")} - ${message}`;
logger.warning = (message) => `${chalkMap.warning("warn ")} - ${message}`;
logger.error = (message) => `${chalkMap.error("error")} - ${message}`;

export default logger;
