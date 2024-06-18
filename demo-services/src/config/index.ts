import { readFileSync } from 'node:fs';
import * as yaml from 'js-yaml';
import { join } from 'path';
import * as process from 'node:process';

const getEnv = () => {
  const env = process.env.NODE_ENV ? '.' + process.env.NODE_ENV : '';
  return env;
};

const getConfig = (): any => {
  const env = getEnv();

  const configFilePath = join(process.cwd(), `application${env}.yaml`);

  const config = readFileSync(configFilePath, {
    encoding: 'utf-8',
  });

  return yaml.load(config);
};

export default getConfig;
