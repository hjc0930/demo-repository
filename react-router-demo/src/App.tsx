import BaseResult from "./components/BaseResult";
import Router from "./routers";
import useAppInitialzation from "./stores/useAppInitialzation";

const App = () => {
  const loading = useAppInitialzation((state) => state.loading);

  return loading ? <BaseResult /> : <Router />;
};

export default App;
