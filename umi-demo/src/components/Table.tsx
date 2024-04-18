import useSort, { SortState } from "@/hooks/useSort";
import React, { useEffect, useMemo, useState } from "react";

interface Column {
  key: string;
  title: string;
  render?: (item: any) => React.ReactNode;
  sorter?: boolean | ((a: any, b: any) => number);
}

interface TableProps {
  data: any[];
  columns: Column[];
  onChange?: (sorter: SortState | null) => void | Promise<void>;
}

const Table: React.FC<TableProps> = ({ data, columns, onChange }) => {
  const { sortedData, sort, handleSort } = useSort({ data, columns });

  useEffect(() => {
    if (onChange) {
      Promise.resolve(onChange(sort)).catch((error) => {
        throw error;
      });
    }
  }, [sort, onChange]);

  return (
    <table>
      <thead>
        <tr>
          {columns.map((column) => (
            <th
              key={column.key}
              onClick={column.sorter ? () => handleSort(column.key) : undefined}
            >
              {column.title}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {sortedData.map((item, index) => (
          <tr key={index}>
            {columns.map((column) => (
              <td key={column.key}>
                {column.render ? column.render(item) : item[column.key]}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default Table;
