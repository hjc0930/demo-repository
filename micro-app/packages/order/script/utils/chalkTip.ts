import nodeChalk from "chalk";
import nodeEmoji from "node-emoji";

export const emoji = nodeEmoji;
export const chalk = nodeChalk;
export const chalkINFO = (v: string) =>
  `${chalk.bgBlueBright.black(" INFO ")} ${chalk.blueBright(v)}`;
export const chalkSUCCESS = (v: string) =>
  `${chalk.bgGreenBright.black(" SUCCESS ")} ${chalk.greenBright(v)}`;
export const chalkERROR = (v: string) =>
  `${chalk.bgRedBright.black(" ERROR ")} ${chalk.redBright(v)}`;
export const chalkWARN = (v: string) =>
  `${chalk.bgHex("#FFA500").black(" WARN ")} ${chalk.hex("#FFA500")(v)}`;
