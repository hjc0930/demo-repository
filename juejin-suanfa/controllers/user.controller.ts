import "reflect-metadata";
import { DIContainer, Injectable } from "../decorator";

class AutoDIContainer extends DIContainer {
  resolve<T>(token: string): T {
    const target = this.services.get(token);
    if (!target) throw new Error(`未注册的服务：${token}`);

    // 自动解析构造函数参数类型
    const paramTypes = Reflect.getMetadata(
      "design:paramtypes",
      target.constructor
    );
    const args = paramTypes.map((type: any) => this.resolve(type.name));
    return new target.constructor(...args);
  }
}

// 使用示例
@Injectable()
class Logger {
  log(message: string) {
    console.log(`[LOG] ${message}`);
  }
}

@Injectable()
class OrderService {
  constructor(private logger: Logger) {}

  createOrder() {
    this.logger.log("订单已创建");
  }
}

const autoContainer = new AutoDIContainer();
autoContainer.register(Logger.name, Logger);
autoContainer.register(OrderService.name, OrderService);

const orderService = autoContainer.resolve<OrderService>("OrderService");
orderService.createOrder(); // 输出：[LOG] 订单已创建
