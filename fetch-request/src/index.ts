import request, { create, requestFactor } from "./request";
import RequestCore from "./core/RequestCore";
import fetchMiddleware from "./middlewares/fetchMiddleware";
import parseResponseMiddleware from "./middlewares/parseResponseMiddleware";
import simpleGetMiddleware from "./middlewares/simpleGetMiddleware";
import simplePostMiddleware from "./middlewares/simplePostMiddleware";

export {
  create,
  requestFactor,
  fetchMiddleware,
  parseResponseMiddleware,
  simpleGetMiddleware,
  simplePostMiddleware,
  RequestCore,
};
export default request;
