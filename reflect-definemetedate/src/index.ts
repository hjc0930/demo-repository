// es7的提案,ts1.5+版本已经支持，可以通过他给类或者类的原型属性上添加元数据
import "reflect-metadata";

const INJECTED = "__inject__";

type Constructor<T = any> = new (...args: any[]) => T;

// 定义一个装饰器，他可以在类的构造函数上定义元数据，定义的元数据是构造函数的所有参数
const Injectable = (): ClassDecorator => (constructor) => {
  Reflect.defineMetadata(
    INJECTED,
    Reflect.getMetadata("design:paramtypes", constructor), // 这里还支持两外两种内置元数据定义，一个是design:type获取属性类型，一个是design:returntype获取返回值类型
    constructor
  );
};

interface IDress {
  name: string;
}
interface IFood {
  name: string;
}
interface IHouse {
  name: string;
}
interface ICar {
  name: string;
}

@Injectable()
class Dress implements IDress {
  name = "LV";
}
@Injectable()
class Food implements IFood {
  name = "michelin";
}
@Injectable()
class Car implements ICar {
  name = "BMW";
}
@Injectable()
class House implements IHouse {
  name = "villa";
  // 家里停着一辆宝马，还有很多lv的包包
  constructor(private car: Car, private dress: Dress) {}
}

@Injectable()
class Person {
  constructor(
    private dress: Dress,
    private food: Food,
    private house: House,
    private car: Car
  ) {}
}

const getInstance = <T>(target: Constructor<T>): T => {
  // 获取所有注入的服务
  const providers = Reflect.getMetadata(INJECTED, target);
  const args =
    providers?.map((provider: Constructor) => {
      return getInstance(provider); // 递归实例化所有依赖
    }) ?? [];
  return new target(...args);
};

console.log(getInstance(Person));
