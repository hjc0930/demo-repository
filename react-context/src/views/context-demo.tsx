import { useSyncExternalStore } from "react";

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

class Store extends BaseStore {
  private initialSelector: any;
  constructor(initialSelector: any) {
    super();
    this.initialSelector = initialSelector;
  }

  public set = (state: any) => {
    this.initialSelector = state;
    this.emitChange();
  };

  public get = () => {
    return this.initialSelector;
  };
}

const createImpl = (initialSelector: any) => {
  const baseStore = new BaseStore();
  const initialValues = initialSelector();

  const set = (selector?: any) => {
    selector?.(initialValues);
    baseStore.emitChange();
  };

  const get = (selector?: any) => {
    return typeof selector === "function"
      ? selector(initalSelector)
      : initalSelector;
  };

  const initialValues = initialSelector?.(set, get);

  return (selector?: any) =>
    useSyncExternalStore(baseStore.subscript, () => {
      // TODO
    });
};

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
