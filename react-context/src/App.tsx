import Child from "./views/Child";
import Child2 from "./views/Child2";
import { Provider } from "./store/userStore";

function App() {
  return (
    <Provider>
      <Child />
      <Child2 />
    </Provider>
  );
}

export default App;
