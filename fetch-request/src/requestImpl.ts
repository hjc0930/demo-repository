import RequestCore from "./core/RequestCore";
import fetchMiddleware from "./middlewares/fetchMiddleware";
import parseResponseMiddleware from "./middlewares/parseResponseMiddleware";
import simpleGetMiddleware from "./middlewares/simpleGetMiddleware";
import simplePostMiddleware from "./middlewares/simplePostMiddleware";
import { Options } from "./types";

const requestImplement = (initialOptions?: Options) => {
  const instance = new RequestCore(initialOptions, [
    simpleGetMiddleware,
    simplePostMiddleware,
    parseResponseMiddleware,
    fetchMiddleware,
  ]);
  return instance;
};

export default requestImplement;
