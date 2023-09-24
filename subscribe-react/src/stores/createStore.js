import {createContext} from "react"

export const createStore = (initState) => {
  let state = initState;

  const listeners = new Set();

  const setState = (partial) => {
    if(typeof partial === "function") {
      state = {...state, ...partial(state)}
    } else {
      state = {...state, ...partial};
    }


    listeners.forEach(listener => {
      listener();
    })
  }


  const getState = () => state;

  const subscribe = (listener) => {
    listeners.add(listener);

    return () => listeners.delete(listener);
  }

  return {
    getState,
    setState,
    subscribe
  }
}

export const AppContext = createContext(null);

const initState = {
  count: 0,
  num: 0
};

export const store = createStore(initState);
