export type Trigger<ContextProps> = (value: ContextProps) => void;

export type Listeners<ContextProps> = Set<Trigger<ContextProps>>;

export class CreateStore<T> {
  public state: T;
  private listeners: Listeners<T> = new Set();

  constructor(state: T) {
    this.state = state;
  }

  public getState = (name?: keyof T) => {
    if (name) {
      return this.state[name];
    }
    return this.state;
  };
  public subscribe = (listener: Trigger<T>) => {
    this.listeners.add(listener);

    return () => {
      this.listeners.delete(listener);
    };
  };

  public emitChange = (value: T) => {
    this.listeners.forEach((listener) => {
      listener(value);
    });
  };
}
