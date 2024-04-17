import { useState } from "react";

const useFetch = (url: string) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    try {
      const response = await fetch(url);
      const data = await response.json();
      setData(data);
    } catch (error: any) {
      setError(error);
    } finally {
      setLoading(false);
    }
  };

  return { fetchData, data, loading, error };
};

export default useFetch;
