import { useEffect } from "react";
import { format } from "date-fns";

function App() {
  useEffect(() => {
    console.log(format(new Date(), "yyyy-MM-dd"));
  }, []);

  return <div className="App">123123</div>;
}

export default App;
