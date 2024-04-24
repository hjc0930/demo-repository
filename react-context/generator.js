const obj = {
  a: 1,
  b: 2,
};

let values = { ...obj };
let stores = { ...obj };

stores = {
  ...stores,
  a: 3,
};

console.log({ values, stores });
