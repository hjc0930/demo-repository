import { useState } from "react";
import Home from "./views/Home";
import User from "./views/User";
import Dragger from "./components/Dagger";
import { Button } from "antd";
import store from './stores'
import { Provider } from 'react-redux'


function App() {
  const [open, setOpen] = useState(false);

  return (
    <Provider store={store}>
      <Home />
      <hr />
      <User />
      <Dragger open={open} onClose={() => setOpen(false)} />
      <Button onClick={() => setOpen(true)}>Open</Button>
    </Provider>
  );
}

export default App;
