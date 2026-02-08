import { MiddlewareType, Options } from "../types";
import { reqStringify, setHeaders } from "../utils";

const simplePostMiddleware: MiddlewareType = async (context: Options, next) => {
  const { method = "get" } = context;

  if (!["post", "put", "patch", "delete"].includes(method.toLowerCase())) {
    await next();
    return;
  }

  const { requestType = "json", data } = context;
  // 数据使用类axios的新字段data, 避免引用后影响旧代码, 如将body stringify多次
  if (data) {
    const dataType = Object.prototype.toString.call(data);
    if (dataType === "[object Object]" || dataType === "[object Array]") {
      if (requestType === "json") {
        setHeaders(context, "Accept", "application/json");
        setHeaders(context, "Content-Type", "application/json;charset=UTF-8");

        context.body = JSON.stringify(data);
      } else if (requestType === "form") {
        setHeaders(context, "Accept", "application/json");
        setHeaders(
          context,
          "Content-Type",
          "application/x-www-form-urlencoded;charset=UTF-8"
        );
        context.body = reqStringify(data, {
          arrayFormat: "repeat",
          strictNullHandling: true,
        });
      }
    } else {
      // order requestType
      setHeaders(context, "Accept", "application/json");

      context.body = data;
    }
  }

  await next();
};

export default simplePostMiddleware;
