import usePagination from "./usePagination";

const Pagination = (props: { page: number; pageSize: number }) => {
  const data = usePagination({
    page: props.page,
    pageSize: props.pageSize,
    data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
  });

  return <div>{data.map((item) => item + ",")}</div>;
};

export default Pagination;
