import { memo } from "react";
import { useCountStore } from "../stores/count";

const Home = memo(() => {
  const { count } = useCountStore();
  return (
    <div>
      <h4>{count}</h4>
      <button
        onClick={() =>
          useCountStore.setState((store) => ({ count: store.count + 1 }))
        }
      >
        Add
      </button>
    </div>
  );
});

export default Home;
