import requestImplement from "./requestImpl";
import { Options, RequestMethodType } from "./types";

const requestFactor = (
  instanceImplement: typeof requestImplement,
  initialOptions: Options = {}
) => {
  const instance = instanceImplement(initialOptions);
  const request = instance.request as RequestMethodType;

  request.interceptors = instance.interceptors;

  const methods = ["get", "post", "put", "delete", "patch", "head", "options"];
  methods.forEach((method) => {
    request[method] = (url: string, options: Options = {}) => {
      return instance.request({ ...options, url, method });
    };
  });

  const create = (options?: Options) => {
    return requestFactor(requestImplement, options).request;
  };

  return { request, create };
};

const { request, create } = requestFactor(requestImplement);

export { requestFactor, create };
export default request;
