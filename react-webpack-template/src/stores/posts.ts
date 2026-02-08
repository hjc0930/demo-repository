import { makeAutoObservable, toJS } from "mobx";
import { RootStore } from ".";

class Posts {
  count = 0;

  constructor() {
    makeAutoObservable(this);
  }

  add = () => {
    this.count += 1;
  };

  getUserInfo = () => {
    return toJS(RootStore.user.userInfo);
  };
}

export { Posts };
