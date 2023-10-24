export type Listener<T> = (state: T) => void;

export type Store<T> = {
  setState: (partial: Partial<T>) => void;
  getState: () => T;
  subscribe: (listener: Listener<T>) => () => void;
};

export function createStore<T>(initState: T): Store<T> {
  let state = initState;

  const listeners = new Set<Listener<T>>();

  const setState = (partial: Partial<T>) => {
    state = { ...state, ...partial };

    listeners.forEach((listener) => {
      listener(state);
    });
  };

  const getState = () => state;

  const subscribe = (listener: Listener<T>) => {
    listeners.add(listener);
    return () => listeners.delete(listener);
  };

  return {
    getState,
    setState,
    subscribe,
  };
}
