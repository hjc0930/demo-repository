import Core from "./core";

const core = new Core();
const request = core.execute.bind(Core);
export {
  Core
}
export default request;