import { memo } from "react";
import { useSelector, useDispatch } from 'react-redux'
import { counterSlice } from '../stores/count'


const Home = memo(() => {
  const count = useSelector((state: any) => state.count.value)
  const dispatch = useDispatch();

  return (
    <div>
      <h4>{count}</h4>
      <button
        onClick={() =>
          dispatch(counterSlice.actions.increment())
        }
      >
        Add
      </button>
    </div>
  );
});

export default Home;
