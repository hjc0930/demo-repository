export default class StateModel {
  constructor(state) {
    this.state = state;
    this.listeners = new Set();
  } // （暴露给状态使用者的）订阅状态的方法
  subscribe(listener) {
    this.listeners.add(listener); // 提供取消订阅的能力
    return () => {
      this.listeners.delete(listener);
    };
  } // 修改数据
  setState(payLoad) {
    const newState = { ...this.state, ...payLoad };
    this.state = newState;
    this.listeners.forEach((l) => l()); // 触发所有“副作用”（试图更新）
  } // 获取数据快照
  getState() {
    return this.state;
  }
}
