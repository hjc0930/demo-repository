import { readFileSync } from "node:fs";
import yaml from "js-yaml";

const readEnvriment = () => {
  const file = readFileSync("./config/env.yaml", "utf8");
  const env = yaml.load(file);
  Object.keys(env).forEach((key) => {
    process.env[key] = env[key];
  });
};

export default readEnvriment;
