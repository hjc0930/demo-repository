import { useState } from "react";
import { useTasksDispatch } from "./TaskContext";

function AddTask() {
  const [text, setText] = useState("");
  const dispatch = useTasksDispatch();

  return (
    <div>
      <input
        type="添加任务"
        value={text}
        onChange={(e) => setText(e.target.value)}
      />
      <button
        onClick={() => {
          setText("");
          dispatch({
            type: "added",
            id: nextId++,
            text: text,
          });
        }}
      >
        添加
      </button>
    </div>
  );
}

let nextId = 3;

export default AddTask;
