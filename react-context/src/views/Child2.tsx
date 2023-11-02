import { useStore } from "../store/userStore";

const Child2 = () => {
  const store = useStore((state) => state.name);

  console.log(store);

  return <div>Child2</div>;
};

export default Child2;
