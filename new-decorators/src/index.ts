// Symbol.metadata ??= Symbol("Symbol.metadata");

interface Context {
  name: string;
  metadata: Record<PropertyKey, unknown>;
}

function setMetadata(_target: any, context: Context) {
  // console.log({ context });
}

class SomeClass {
  @setMetadata
  foo = 123;

  @setMetadata
  accessor bar = "hello!";

  @setMetadata
  baz() {}
}

const ourMetadata = SomeClass[Symbol.metadata];

console.log(SomeClass);

// console.log(JSON.stringify(ourMetadata));
