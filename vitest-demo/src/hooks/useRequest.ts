import { useState } from "react";

const useRequest = (service: () => Promise<any>) => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState();

  const run = async () => {
    try {
      setLoading(true);
      const res = await service();
      setData(res);
    } finally {
      setLoading(false);
    }
  };

  return {
    run,
    loading,
    data,
  };
};

export default useRequest;
