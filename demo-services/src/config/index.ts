import * as process from 'node:process';
import * as path from 'node:path';

const getEnv = () => {
  const env = process.env.NODE_ENV ? '.' + process.env.NODE_ENV : '';
  return env;
};

const getConfigPath = (): string => {
  const env = getEnv();

  return path.resolve(process.cwd(), '.env' + env);
};

export default getConfigPath;
