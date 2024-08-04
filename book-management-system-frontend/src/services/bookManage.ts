import request from "../utils/request";

export const getBookManage = (
  name?: string
): Promise<
  {
    id: number;
    author: string;
    name: string;
    description: string;
    cover: string;
  }[]
> => {
  return request.get("book", {
    params: {
      name,
    },
  });
};

export const deleteBookManage = (id: number) => {
  return request.delete(`book/${id}`);
};

export const createBookManage = (data: {
  name: string;
  author: string;
  description: string;
  cover: string;
}) => {
  return request.post("book", data);
};

export const editBookManage = (data: {
  id: number;
  name: string;
  author: string;
  description: string;
  cover: string;
}) => {
  return request.patch("book", data);
};
