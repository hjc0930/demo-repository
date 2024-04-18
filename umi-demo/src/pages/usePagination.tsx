import { useState } from "react";

interface ParamsType {
  page: number;
  pageSize: number;
  data: any[];
}

const usePagination = (params: ParamsType) => {
  const { page, pageSize, data } = params;

  const [newData, setNewData] = useState<any[]>(() => {
    console.log(page, pageSize);

    return data.slice((page - 1) * pageSize, pageSize);
  });

  return newData;
};

export default usePagination;
