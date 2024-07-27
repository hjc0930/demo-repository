const wrapPromise = (fn: (resolve: any, reject: any) => VoidFunction) => {
  const { promise, resolve, reject } = Promise.withResolvers();

  const result: any = fn(resolve, reject);

  result.then = (fill: any, reject: any) => {
    promise.then(fill, reject);
  };

  result.promise = promise;

  return result;
};

export default wrapPromise;
