import { useState } from "react";

function Home() {
  const [count] = useState(0);

  return (
    <>
      <p>count: {count}</p>
    </>
  );
}
export default Home;
