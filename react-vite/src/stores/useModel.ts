import { useSyncExternalStore } from "react";

const useModel = function (store) {
  const state = useSyncExternalStore(
    store.subscribe.bind(store),
    store.getState.bind(store)
  );

  return {
    useSelector(selector) {
      return selector(state);
    },
    useDispatch() {
      return (payLoad) => {
        store.setState(payLoad);
      };
    },
  };
};
export default useModel;
