import { useAppDispatch, useAppStore } from "../../store/AppStore";

function Child1() {
  const { count } = useAppStore();
  return (
    <div>
      <h2>Child1</h2>
      <p>{count}</p>
    </div>
  );
}

function Child2() {
  const dispatch = useAppDispatch();
  return (
    <div>
      <h2>Child2</h2>
      <button
        onClick={() => {
          dispatch({
            count: 1,
          });
        }}
      >
        Add
      </button>
    </div>
  );
}

function Child3() {
  console.log(123123);

  return (
    <div>
      <h2>Child3</h2>
    </div>
  );
}

function ReactContext() {
  return (
    <>
      <Child1 />
      <Child2 />
      <Child3 />
    </>
  );
}

export default ReactContext;
