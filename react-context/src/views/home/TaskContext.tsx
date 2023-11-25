import {
  PropsWithChildren,
  createContext,
  useCallback,
  useContext,
  useReducer,
} from "react";

export interface TaskType {
  id: number;
  text: string;
  done: boolean;
}

const initalTasks: TaskType[] = [
  { id: 0, text: "Philosopher’s Path", done: true },
  { id: 1, text: "Visit the temple", done: false },
  { id: 2, text: "Drink matcha", done: false },
];

function taskReducer(
  tasks: TaskType[],
  action: {
    type: string;
    task?: TaskType;
  } & TaskType
) {
  switch (action.type) {
    case "added": {
      return [
        ...tasks,
        {
          id: action.id,
          text: action.text,
          done: false,
        },
      ];
    }
    case "changed": {
      return tasks.map((t) => {
        if (t.id === action?.task?.id) {
          return action.task;
        } else {
          return t;
        }
      });
    }
    case "deleted": {
      return tasks.filter((t) => t.id !== action.id);
    }

    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}

const TasksContext = createContext(initalTasks);

const TasksDispatchContext = createContext<any>(null);

export function TaskProvider({ children }: PropsWithChildren) {
  const [tasks, dispatch] = useReducer(taskReducer, initalTasks);

  return (
    <TasksContext.Provider value={tasks}>
      <TasksDispatchContext.Provider value={dispatch}>
        {children}
      </TasksDispatchContext.Provider>
    </TasksContext.Provider>
  );
}

export function useTasks() {
  return useContext(TasksContext);
}

export function useTasksDispatch() {
  return useCallback(useContext(TasksDispatchContext), []);
}
