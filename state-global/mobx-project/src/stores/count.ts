import { makeAutoObservable } from "mobx"

class CountStore {
  count = 0;
  constructor() {
    makeAutoObservable(this);
  }

  add = () => {
    this.count += 1;
  }
}

export const countStore = new CountStore();
