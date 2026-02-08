import { Method, MiddlewareType, Options } from "../types";
import formatUrl from "../utils/formatUrl";

// 对请求参数做处理，实现 query 简化
const simpleGetNiddleware: MiddlewareType = async (context: Options, next) => {
  // Change method to uppercase
  context.method = (
    context.method ? context.method.toUpperCase() : "GET"
  ) as Method;

  // 设置 credentials 默认值为 same-origin，确保当开发者没有设置时，各浏览器对请求是否发送 cookies 保持一致的行为
  // - omit: 从不发送cookies.
  // - same-origin: 只有当URL与响应脚本同源才发送 cookies、 HTTP Basic authentication 等验证信息.(浏览器默认值,在旧版本浏览器，例如safari 11依旧是omit，safari 12已更改)
  // - include: 不论是不是跨域的请求,总是发送请求资源域在本地的 cookies、 HTTP Basic authentication 等验证信息.
  context.credentials = context.credentials || "same-origin";

  const url = formatUrl(context);
  context.url = url;
  await next();
};

export default simpleGetNiddleware;
