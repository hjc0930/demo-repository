import { useRequest } from "ahooks";
import { useState } from "react";

const Demo = () => {
  const [ren, setRen] = useState("");
  const { runAsync } = useRequest(
    async () => {
      return 1;
    },
    {
      manual: true,
    }
  );

  const onFetch = async () => {
    const res = await runAsync(1, 2);
    setRen(res);
  };
  return (
    <>
      <button onClick={onFetch}>Click</button>
      <p>{ren}</p>
    </>
  );
};

export default Demo;
