import { useState, useEffect } from "react";

interface UsePaginationProps {
  data: any[];
  defaultPage?: number;
  defaultPageSize?: number;
  page?: number;
  pageSize?: number;
}

interface UsePaginationReturn {
  paginatedData: any[];
  currentPage: number;
  totalPages: number;
  pageSize: number;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
}

const usePagination = ({
  data,
  defaultPage,
  defaultPageSize,
  page: externalPage,
  pageSize: externalPageSize,
}: UsePaginationProps): UsePaginationReturn => {
  const [currentPage, setPage] = useState(externalPage || defaultPage || 1);
  const [pageSize, setPageSize] = useState(
    externalPageSize || defaultPageSize || 10
  );

  useEffect(() => {
    if (externalPage !== undefined) {
      setPage(externalPage);
    }
  }, [externalPage]);

  useEffect(() => {
    if (externalPageSize !== undefined) {
      setPageSize(externalPageSize);
    }
  }, [externalPageSize]);

  const totalPages = Math.ceil(data.length / pageSize);
  const paginatedData = data.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  return {
    paginatedData,
    currentPage,
    totalPages,
    pageSize,
    setPage,
    setPageSize,
  };
};

export default usePagination;
