import { useContext } from "react";
import { useAppStore } from "../App";

const Home = () => {
  const [age, setQuery] = useAppStore((state) => state.age);

  return (
    <div>
      <h4>{age}</h4>
      <button
        onClick={() => {
          setQuery({
            age: 18,
          });
        }}
      >
        Set
      </button>
    </div>
  );
};

export default Home;
