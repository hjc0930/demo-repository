import { makeAutoObservable } from "mobx";

class User {
  userInfo: { id: number; name: string }[] = [];

  constructor() {
    makeAutoObservable(this);
  }

  init = () => {
    this.userInfo = [
      { id: 1, name: "123" },
      { id: 2, name: "234" },
    ];
  };
}

export { User };
