import "./App.css";
import { Button } from "antd";
import useEventEmitter from "./hook/useEventEmitter";
import Home from "./views/Home";
import { RouterEventEmitterProvider } from "./store/routerEventEmitterContext";

const App = () => {
  const event = useEventEmitter();
  const handleClick = () => {
    event.emit("API:AUTH", "buttonClick");
  };

  return (
    <RouterEventEmitterProvider value={event}>
      <Button onClick={handleClick}>Click</Button>
      <Home />
    </RouterEventEmitterProvider>
  );
};

export default App;
