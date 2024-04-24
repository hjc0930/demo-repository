import * as crypto from 'crypto';

const generatorMd5 = (str: string) => {
  const hash = crypto.createHash('md5');
  hash.update(str);
  return hash.digest('hex');
};

export default generatorMd5;
