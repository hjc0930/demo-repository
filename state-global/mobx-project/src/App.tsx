import rootStotes, { RootStoresContext } from "./stores";
import Routers from "./routers";

function App() {
  return (
    <RootStoresContext.Provider value={rootStotes}>
      <Routers />
    </RootStoresContext.Provider>
  );
}

export default App;
