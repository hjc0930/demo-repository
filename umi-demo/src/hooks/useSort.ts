import { useState } from "react";

interface Column {
  key: string;
  sorter?: boolean | ((a: any, b: any) => number);
}

interface UseSortProps {
  data: any[];
  columns: Column[];
}

export interface SortState {
  key: string;
  direction: "asc" | "desc" | undefined;
}

const useSort = ({ data, columns }: UseSortProps) => {
  const [sort, setSort] = useState<SortState | null>(null);

  let sortedData = [...data];
  if (sort) {
    const sortingColumn = columns.find((column) => column.key === sort.key);

    if (sortingColumn?.sorter && sortingColumn?.sorter !== true) {
      if (sort.direction === "asc") {
        sortedData = sortedData.toSorted(sortingColumn.sorter);
      } else if (sort.direction === "desc") {
        sortedData = sortedData.toReversed();
      } else {
        sortedData = [...data];
      }
    }
  }

  const handleSort = (key: string) => {
    setSort((prevSort) => {
      if (prevSort?.key === key && prevSort.direction !== undefined) {
        return {
          key,
          direction: prevSort.direction === "asc" ? "desc" : undefined,
        };
      }
      return { key, direction: "asc" };
    });
  };

  return { sortedData, handleSort, sort };
};

export default useSort;
