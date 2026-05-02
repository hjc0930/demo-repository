import { createStore } from "@tanstack/react-store";

type CounterActions = {
  add: () => void;
  reset: () => void;
};

const countStore = createStore<number, CounterActions>(0, ({ setState }) => ({
  add: () => {
    console.log("This is a promise test.");
    setState((val) => val + 1);
  },
  reset: () => {
    console.log("This is a promise test.");

    setState(() => 0);
  },
}));

export default countStore;
