import { useState } from "react";
import Home from "./views/Home";
import User from "./views/User";
import Dragger from "./components/Dagger";
import { Button } from "antd";

function App() {
  const [open, setOpen] = useState(false);

  return (
    <>
      <Home />
      <hr />
      <User />
      <Dragger open={open} onClose={() => setOpen(false)} />
      <Button onClick={() => setOpen(true)}>Open</Button>
    </>
  );
}

export default App;
