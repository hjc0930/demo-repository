import isDev from "@/hooks/useFetch/src/utils/isDev";
import { useLatest } from "ahooks";
import { Badge } from "antd";
import { useEffect, useRef, useState } from "react";

const arr = [1, 2, 3, 4, 5, 6];
function Home() {
  const [count, setCount] = useState(0);

  return (
    <>
      <p>count: {count}</p>
    </>
  );
}
export default Home;
