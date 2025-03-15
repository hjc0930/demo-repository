export const getUser = () => {
  return new Promise((res) => {
    setTimeout(() => {
      res({
        data: {
          user: {
            id: 1,
            name: "jiacheng",
          },
          permission: ["admin"],
        },
      });
    }, 1500);
  });
};
