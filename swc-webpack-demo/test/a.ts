const prefix = process.env.PREFIX!;

export const getEnvironment = () => {
  return process.env.PREFIX;
};

export const getEnvironmentVariable = () => {
  return prefix;
};
