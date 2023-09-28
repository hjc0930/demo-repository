import { useSyncExternalStore } from "react";

let nextId = 0;
let todos = [{ id: nextId++, text: "Todo #1" }];
let listeners: any[] = [];

export const todoStores = {
  addTodo() {
    todos = [...todos, { id: nextId++, text: "Todo #" + nextId }];
    emitChange();
  },

  subscribe(listener: any) {
    listeners = [...listeners, listener];
    return () => {
      listeners = listeners.filter((l) => l !== listener);
    };
  },

  getTodos() {
    return todos;
  },
};

const emitChange = () => {
  for (let listener of listeners) {
    listener();
  }
};

const App2 = () => {
  const todoList = useSyncExternalStore(
    todoStores.subscribe,
    todoStores.getTodos
  );
  return (
    <div>
      <button
        onClick={() => {
          todoStores.addTodo();
        }}
      >
        Add
      </button>
      <ul>
        {todoList.map((item) => (
          <li key={item.id}>{item.text}</li>
        ))}
      </ul>
    </div>
  );
};

export default App2;
