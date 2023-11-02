import { useStore } from "../store/userStore";

const Child = () => {
  const { syncStore: id, set } = useStore((state) => state.id);

  return (
    <div>
      Child
      <h2>{id}</h2>
      <button
        onClick={() => {
          set({
            id: id + 1,
          });
        }}
      >
        Add
      </button>
    </div>
  );
};

export default Child;
