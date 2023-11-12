import Router from "./router";
import AppStore from "./store/AppStore";

function App() {
  return (
    <AppStore>
      <Router />
    </AppStore>
  );
}

export default App;
