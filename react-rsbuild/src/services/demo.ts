const basePrefix = "PREFIX";

const getPrefix = (field: string, ...args: string[]) => {
  const basePrefix = process.env[field];

  if (!basePrefix) {
    return "";
  }
  return `${basePrefix}${args.join("")}`;
};

export const getEnvironment = () => {
  const prefix = getPrefix(basePrefix);
  return prefix;
};

export const getEnvironmentVariable = () => {
  const prefix = getPrefix(basePrefix, "/a", "/b");
  return prefix;
};
