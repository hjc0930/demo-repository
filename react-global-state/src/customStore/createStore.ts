export type Listener<T> = (state: T) => void;

export type Store<T> = {
  set: (store: T) => void;
  get: () => T;
  subscribe: (listener: Listener<T>) => () => void;
};

export const createStore = <T>(initState: T): Store<T> => {
  let state = initState;

  const listeners = new Set<Listener<T>>();

  const set = (s: T) => {
    state = { ...state, ...s };

    listeners.forEach((listeners) => {
      listeners(state);
    });
  };

  const get = () => state;

  const subscribe = (listener: Listener<T>) => {
    listeners.add(listener);

    return () => listeners.delete(listener);
  };

  return {
    get,
    set,
    subscribe,
  };
};
