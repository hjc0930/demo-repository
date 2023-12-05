const chalkColor = {
  bright: "\x1B[1m", // 亮色
  grey: "\x1B[2m", // 灰色
  italic: "\x1B[3m", // 斜体
  underline: "\x1B[4m", // 下划线
  reverse: "\x1B[7m", // 反向
  hidden: "\x1B[8m", // 隐藏
  black: "\x1B[30m", // 黑色
  red: "\x1B[31m", // 红色
  green: "\x1B[32m", // 绿色
  yellow: "\x1B[33m", // 黄色
  blue: "\x1B[34m", // 蓝色
  magenta: "\x1B[35m", // 品红
  cyan: "\x1B[36m", // 青色
  white: "\x1B[37m", // 白色
  blackBG: "\x1B[40m", // 背景色为黑色
  redBG: "\x1B[41m", // 背景色为红色
  greenBG: "\x1B[42m", // 背景色为绿色
  yellowBG: "\x1B[43m", // 背景色为黄色
  blueBG: "\x1B[44m", // 背景色为蓝色
  magentaBG: "\x1B[45m", // 背景色为品红
  cyanBG: "\x1B[46m", // 背景色为青色
  whiteBG: "\x1B[47m", // 背景色为白色
};
/**
 * @type {Record<keyof typeof chalkColor, (message: string) => string>}
 */
const chalk = {
  bright: function (message) {
    throw new Error("Function not implemented.");
  },
  grey: function (message) {
    throw new Error("Function not implemented.");
  },
  italic: function (message) {
    throw new Error("Function not implemented.");
  },
  underline: function (message) {
    throw new Error("Function not implemented.");
  },
  reverse: function (message) {
    throw new Error("Function not implemented.");
  },
  hidden: function (message) {
    throw new Error("Function not implemented.");
  },
  black: function (message) {
    throw new Error("Function not implemented.");
  },
  red: function (message) {
    throw new Error("Function not implemented.");
  },
  green: function (message) {
    throw new Error("Function not implemented.");
  },
  yellow: function (message) {
    throw new Error("Function not implemented.");
  },
  blue: function (message) {
    throw new Error("Function not implemented.");
  },
  magenta: function (message) {
    throw new Error("Function not implemented.");
  },
  cyan: function (message) {
    throw new Error("Function not implemented.");
  },
  white: function (message) {
    throw new Error("Function not implemented.");
  },
  blackBG: function (message) {
    throw new Error("Function not implemented.");
  },
  redBG: function (message) {
    throw new Error("Function not implemented.");
  },
  greenBG: function (message) {
    throw new Error("Function not implemented.");
  },
  yellowBG: function (message) {
    throw new Error("Function not implemented.");
  },
  blueBG: function (message) {
    throw new Error("Function not implemented.");
  },
  magentaBG: function (message) {
    throw new Error("Function not implemented.");
  },
  cyanBG: function (message) {
    throw new Error("Function not implemented.");
  },
  whiteBG: function (message) {
    throw new Error("Function not implemented.");
  },
};

for (const key in chalkColor) {
  chalk[key] = (message) => chalkColor[key] + message + "\x1B[0m";
}

export default chalk;
