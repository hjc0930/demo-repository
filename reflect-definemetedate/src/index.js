var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
// es7的提案,ts1.5+版本已经支持，可以通过他给类或者类的原型属性上添加元数据
import "reflect-metadata";
const INJECTED = "__inject__";
// 定义一个装饰器，他可以在类的构造函数上定义元数据，定义的元数据是构造函数的所有参数
const Injectable = () => (constructor) => {
    Reflect.defineMetadata(INJECTED, Reflect.getMetadata("design:paramtypes", constructor), // 这里还支持两外两种内置元数据定义，一个是design:type获取属性类型，一个是design:returntype获取返回值类型
        constructor);
};
let Dress = class Dress {
    name = "LV";
};
Dress = __decorate([
    Injectable()
], Dress);
let Food = class Food {
    name = "michelin";
};
Food = __decorate([
    Injectable()
], Food);
let Car = class Car {
    name = "BMW";
};
Car = __decorate([
    Injectable()
], Car);
let House = class House {
    car;
    dress;
    name = "villa";
    // 家里停着一辆宝马，还有很多lv的包包
    constructor(car, dress) {
        this.car = car;
        this.dress = dress;
    }
};
House = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [Car, Dress])
], House);
let Person = class Person {
    dress;
    food;
    house;
    car;
    constructor(dress, food, house, car) {
        this.dress = dress;
        this.food = food;
        this.house = house;
        this.car = car;
    }
};
Person = __decorate([
    Injectable(),
    __metadata("design:paramtypes", [Dress,
        Food,
        House,
        Car])
], Person);
const getInstance = (target) => {
    // 获取所有注入的服务
    const providers = Reflect.getMetadata(INJECTED, target);
    const args = providers?.map((provider) => {
        return getInstance(provider); // 递归实例化所有依赖
    }) ?? [];
    return new target(...args);
};
console.log(getInstance(Person));
