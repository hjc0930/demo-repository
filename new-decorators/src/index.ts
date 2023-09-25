type Class<T = any> = new (...args: any[]) => T;

class Container {
  private services: Map<string, Class> = new Map();

  register<T>(serviceIdentifier: string, target: Class<T>) {
    this.services.set(serviceIdentifier, target);
  }

  resolve<T>(serviceIdentifier: string): T {
    const target = this.services.get(serviceIdentifier);
    if (!target) {
      throw new Error(`Service not registered: ${serviceIdentifier}`);
    }

    const dependencies = Reflect.getMetadata("design:paramtypes", target) || [];
    const resolvedDependencies = dependencies.map((dep: Class) =>
      this.resolve(dep.name)
    );
    return new target(...resolvedDependencies);
  }
}

// 装饰器工厂函数
function Injectable(target: Class) {
  // 可以在这里完成对该类的初始化操作
}

// 在类上使用@Injectable装饰器，表示该类可被注入到容器中
@Injectable
class UserService {
  constructor() {}

  getUser() {
    return "User";
  }
}

@Injectable
class LoggerService {
  constructor() {}

  log(message: string) {
    console.log(`[Logger]: ${message}`);
  }
}

const container = new Container();
container.register("userService", UserService);
container.register("loggerService", LoggerService);

const userService = container.resolve<UserService>("userService");
const loggerService = container.resolve<LoggerService>("loggerService");

console.log(userService.getUser()); // 输出: User
loggerService.log("Logging message"); // 输出: [Logger]: Logging message
