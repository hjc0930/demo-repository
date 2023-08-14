interface Context {
  name: string;
  metadata: Record<PropertyKey, unknown>;
}

function meta(key, metadata) {
  return (_, context: Context) => {
    console.log(context);
    context.metadata = {
      [key]: metadata,
    };
  };
}

class SomeClass {
  @meta("a", "field:foo")
  foo = 123;
}
