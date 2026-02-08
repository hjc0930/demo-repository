import { Options } from "../types";
import { isArray, isDate, isObject, isURLSearchParams } from "./typeAsserts";
import { forEach2ObjArr, reqStringify } from "./utils";

const paramsSerialize = (
  params: any,
  paramsSerializer?: (args: any) => any
) => {
  let serializedParams;
  let jsonStringifiedParams;
  // 支持参数自动拼装，其他 method 也可用，不冲突
  if (params) {
    if (paramsSerializer) {
      serializedParams = paramsSerializer(params);
    } else if (isURLSearchParams(params)) {
      serializedParams = params.toString();
    } else {
      if (isArray(params)) {
        jsonStringifiedParams = [];
        forEach2ObjArr(params, function (item) {
          if (item === null || typeof item === "undefined") {
            jsonStringifiedParams.push(item);
          } else {
            jsonStringifiedParams.push(
              isObject(item) ? JSON.stringify(item) : item
            );
          }
        });
        // a: [1,2,3] => a=1&a=2&a=3
        serializedParams = reqStringify(jsonStringifiedParams, {
          arrayFormat: "repeat",
          strictNullHandling: true,
        });
      } else {
        jsonStringifiedParams = {};
        forEach2ObjArr(params, function (value, key) {
          let jsonStringifiedValue = value;
          if (value === null || typeof value === "undefined") {
            jsonStringifiedParams[key] = value;
          } else if (isDate(value)) {
            jsonStringifiedValue = value.toISOString();
          } else if (isArray(value)) {
            jsonStringifiedValue = value;
          } else if (isObject(value)) {
            jsonStringifiedValue = JSON.stringify(value);
          }
          jsonStringifiedParams[key] = jsonStringifiedValue;
        });
        const tmp = reqStringify(jsonStringifiedParams, {
          arrayFormat: "repeat",
          strictNullHandling: true,
        });
        serializedParams = tmp;
      }
    }
  }
  return serializedParams;
};

const formatUrl = (context: Options) => {
  const { baseURL = "", url = "", params = {} } = context;

  const mergeUrl = baseURL + url;
  // TODO: 添加自定义格式化函数
  let serializedParams = paramsSerialize(params);

  if (serializedParams) {
    const urlSign = url.indexOf("?") !== -1 ? "&" : "?";
    return `${mergeUrl}${urlSign}${serializedParams}`;
  } else {
    return mergeUrl;
  }
};

export default formatUrl;
