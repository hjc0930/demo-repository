// 参考自 puck-core 请求库的插件机制
import { Context } from '../types';
import compose, { Middlewares } from './compose';

class Onion {
  private middlewares: Middlewares[];

  constructor() {
    this.middlewares = [];
  }
  static coreMiddlewares = []; // 内核中间件

  use(newMiddleware: Middlewares) {
    // 实例中间件
    this.middlewares.push(newMiddleware);

    return this;
  }

  execute(context: Context) {
    const fn = compose([
      ...this.middlewares,
      ...Onion.coreMiddlewares,
    ]);
    return fn(context);
  }
}

export default Onion;
