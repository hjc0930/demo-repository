function Greeter(value, context) {
    if (context.kind === "class") {
        value.prototype.greet = function () {
            console.log("你好");
        };
    }
}
@Greeter
class User {
}
let u = new User();
u.greet(); // "你好"
