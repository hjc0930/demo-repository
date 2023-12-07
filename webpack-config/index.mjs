const obj = {};
obj.__proto__.toString = () => {
  console.log(123213);
};

console.log(obj);
