export const getUser = () => {
  return fetch("http://localhost:8080/user/list").then((res) => res.json());
};
