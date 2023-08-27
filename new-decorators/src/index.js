"use strict";
// Symbol.metadata ??= Symbol("Symbol.metadata");
var __esDecorate = (this && this.__esDecorate) || function (ctor, descriptorIn, decorators, contextIn, initializers, extraInitializers) {
    function accept(f) { if (f !== void 0 && typeof f !== "function") throw new TypeError("Function expected"); return f; }
    var kind = contextIn.kind, key = kind === "getter" ? "get" : kind === "setter" ? "set" : "value";
    var target = !descriptorIn && ctor ? contextIn["static"] ? ctor : ctor.prototype : null;
    var descriptor = descriptorIn || (target ? Object.getOwnPropertyDescriptor(target, contextIn.name) : {});
    var _, done = false;
    for (var i = decorators.length - 1; i >= 0; i--) {
        var context = {};
        for (var p in contextIn) context[p] = p === "access" ? {} : contextIn[p];
        for (var p in contextIn.access) context.access[p] = contextIn.access[p];
        context.addInitializer = function (f) { if (done) throw new TypeError("Cannot add initializers after decoration has completed"); extraInitializers.push(accept(f || null)); };
        var result = (0, decorators[i])(kind === "accessor" ? { get: descriptor.get, set: descriptor.set } : descriptor[key], context);
        if (kind === "accessor") {
            if (result === void 0) continue;
            if (result === null || typeof result !== "object") throw new TypeError("Object expected");
            if (_ = accept(result.get)) descriptor.get = _;
            if (_ = accept(result.set)) descriptor.set = _;
            if (_ = accept(result.init)) initializers.unshift(_);
        }
        else if (_ = accept(result)) {
            if (kind === "field") initializers.unshift(_);
            else descriptor[key] = _;
        }
    }
    if (target) Object.defineProperty(target, contextIn.name, descriptor);
    done = true;
};
var __runInitializers = (this && this.__runInitializers) || function (thisArg, initializers, value) {
    var useValue = arguments.length > 2;
    for (var i = 0; i < initializers.length; i++) {
        value = useValue ? initializers[i].call(thisArg, value) : initializers[i].call(thisArg);
    }
    return useValue ? value : void 0;
};
function setMetadata(_target, context) {
    // console.log({ context });
}
let SomeClass = (() => {
    let _instanceExtraInitializers = [];
    let _foo_decorators;
    let _foo_initializers = [];
    let _bar_decorators;
    let _bar_initializers = [];
    let _baz_decorators;
    return class SomeClass {
        static {
            const _metadata = typeof Symbol === "function" && Symbol.metadata ? Object.create(null) : void 0;
            _foo_decorators = [setMetadata];
            _bar_decorators = [setMetadata];
            _baz_decorators = [setMetadata];
            __esDecorate(this, null, _bar_decorators, { kind: "accessor", name: "bar", static: false, private: false, access: { has: obj => "bar" in obj, get: obj => obj.bar, set: (obj, value) => { obj.bar = value; } }, metadata: _metadata }, _bar_initializers, _instanceExtraInitializers);
            __esDecorate(this, null, _baz_decorators, { kind: "method", name: "baz", static: false, private: false, access: { has: obj => "baz" in obj, get: obj => obj.baz }, metadata: _metadata }, null, _instanceExtraInitializers);
            __esDecorate(null, null, _foo_decorators, { kind: "field", name: "foo", static: false, private: false, access: { has: obj => "foo" in obj, get: obj => obj.foo, set: (obj, value) => { obj.foo = value; } }, metadata: _metadata }, _foo_initializers, _instanceExtraInitializers);
            if (_metadata) Object.defineProperty(this, Symbol.metadata, { enumerable: true, configurable: true, writable: true, value: _metadata });
        }
        foo = (__runInitializers(this, _instanceExtraInitializers), __runInitializers(this, _foo_initializers, 123));
        #bar_accessor_storage = __runInitializers(this, _bar_initializers, "hello!");
        get bar() { return this.#bar_accessor_storage; }
        set bar(value) { this.#bar_accessor_storage = value; }
        baz() { }
    };
})();
const ourMetadata = SomeClass[Symbol.metadata];
console.dir(SomeClass);
// console.log(JSON.stringify(ourMetadata));
