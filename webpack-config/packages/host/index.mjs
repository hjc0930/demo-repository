import { merge } from "lodash-es";

const obj1 = {
  a: 1,
  b: {
    c: 1,
    aaa: {
      c: 3,
    },
  },
};

const obj2 = {
  b: {
    d: 2,
    aaa: {
      a: 1,
      b: 2,
    },
  },
};

console.log(merge(obj1, obj2));
