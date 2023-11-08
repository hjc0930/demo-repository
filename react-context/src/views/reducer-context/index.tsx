import { useState } from "react";
import AddTask from "./AddTask";
import TaskList from "./TaskList";
import { TaskProvider } from "./TaskContext";

function ReducerContext() {
  return (
    <TaskProvider>
      <h1>布拉格的行程安排</h1>
      <AddTask />
      <TaskList />
    </TaskProvider>
  );
}

export default ReducerContext;
