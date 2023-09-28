import { observer } from "mobx-react-lite";
import useGlobalStore from "../hooks/useGlobalStore";

const Home = () => {
  const { count, add } = useGlobalStore("count");


  return (
    <div>
      <h4>{count}</h4>
      <button onClick={() => add()}>Add</button>
    </div>
  );
};

export default observer(Home);
