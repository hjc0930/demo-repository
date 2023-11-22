import { useSyncExternalStore } from "react";
import { T } from "vitest/dist/reporters-5f784f42";

class BaseStore {
  protected listerners: Set<Function> = new Set();

  public subscript = (listerner: Function) => {
    this.listerners.add(listerner);

    return () => {
      this.listerners.delete(listerner);
    };
  };
  public emitChange = () => {
    this.listerners.forEach((listener) => listener());
  };
}

type SelectorType<T, R extends T = any> = (state: T) => R;

class Store<T> extends BaseStore {
  private initialSelector: T;
  constructor(initialSelector: T) {
    super();
    this.initialSelector = initialSelector;
  }

  public set = (selector: any) => {
    if (typeof selector === "function") {
      this.initialSelector = selector(this.initialSelector);
    }
    this.emitChange();
  };

  public get = (selector?: SelectorType<T>) => {
    return typeof selector === "function"
      ? selector(this.initialSelector)
      : this.initialSelector;
  };
}

function createImpl<T>(
  initialSelector?: (set?: any, get?: any) => Record<string, any>
) {
  const baseStore = new BaseStore();

  initialSelector?.(
    (selector?: SelectorType<any>) => {
      selector?.({});
      baseStore.emitChange();
    },
    (selector?: SelectorType<any>) => {
      return selector?.({}) ?? {};
    }
  );
  // const stores = new Store();

  return (selector?: SelectorType<T>) =>
    useSyncExternalStore(stores.subscript, () => {
      return stores.get(selector);
    });
}

const create = (initalSelector: any) => {
  return createImpl(initalSelector);
};

function ContextDemo() {
  const [form] = useForm();
  return (
    <div>
      <h1>{form.initialValue.b}</h1>
      <button onClick={form.addValue}>Add</button>
    </div>
  );
}

export default ContextDemo;
