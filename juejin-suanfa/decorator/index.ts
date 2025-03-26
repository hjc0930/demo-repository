// 依赖容器（简易实现）
export class DIContainer {
  private services: Map<string, any> = new Map();

  register<T>(token: string, implementation: new (...args: any[]) => T) {
    this.services.set(token, new implementation());
  }

  resolve<T>(token: string): T {
    const service = this.services.get(token);
    if (!service) throw new Error(`未注册的服务：${token}`);
    return service;
  }
}

// 全局容器实例
export const container = new DIContainer();

// 类装饰器：标记可注入的类
export function Injectable(token?: string) {
  return (target: any) => {
    container.register(token ?? target?.name, target);
  };
}

// 参数装饰器：注入依赖
export function Inject(token: string) {
  return (target: any, key: string, index: number) => {
    target.__dependencies = target.__dependencies || [];
    target.__dependencies[index] = token;
  };
}
