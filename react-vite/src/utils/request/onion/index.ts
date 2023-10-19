// 参考自 puck-core 请求库的插件机制
import { Context } from "../types";
import compose from "./compose";
import type { Middlewares } from "../types";

class Onion {
  // 实例中间件
  private middlewares: Middlewares[];

  static coreMiddlewares: Middlewares[] = []; // 全局中间件

  constructor() {
    this.middlewares = [];
  }

  use(newMiddleware: Middlewares) {
    // 实例中间件
    this.middlewares.push(newMiddleware);

    return this;
  }

  execute(context: Context) {
    const fn = compose([...Onion.coreMiddlewares, ...this.middlewares]);
    return fn(context);
  }
}

export default Onion;
