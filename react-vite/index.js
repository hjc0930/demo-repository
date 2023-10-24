function warpperMiddwares(ctx) {
  return function (middleWares) {
    return () => {
      const dispatch = (i) => {
        let middleware = middleWares[i];

        if (!middleware) {
          return Promise.resolve();
        }
        return Promise.resolve(
          middleware(ctx, () => {
            return dispatch(i + 1);
          })
        );
      };

      return dispatch(0);
    };
  };
}

async function parseResponseMiddleware(ctx, next) {
  await next();
}

async function abortRequestMiddleware(ctx, next) {
  ctx.options.signal = AbortSignal.timeout(3000);
  await next();
}

async function fetchMiddleware(ctx, next) {
  const { url, ...restOptions } = ctx.options;

  const request = new Request(url, restOptions);
  ctx.request = request.clone();

  const response = await fetch(request);

  ctx.response = response;
  await next();
}

const middleWares = [parseResponseMiddleware, fetchMiddleware];

const context = {
  options: {
    url: "http://localhost:3000/",
  },
  request: null,
  response: null,
};
const compose = warpperMiddwares(context);

const finalFn = compose(middleWares);

finalFn().then(() => {
  console.log({ context }, 123);
});
