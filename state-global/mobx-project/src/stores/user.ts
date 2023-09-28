

import { makeAutoObservable, runInAction } from "mobx";
import type { UserInfo } from "../entitys/user";
import { getUserList } from "../services/user";
import rootStotes from ".";

class UserStore {
  userInof: UserInfo[] = [];
  loading = true;
  userCount = 0;

  constructor() {
    makeAutoObservable(this)
  }

  init = async () => {
    this.toggerLoading(true);
    const result = await getUserList();
    runInAction(() => {
      this.userInof = result;
    })
    this.toggerLoading(false);
  }

  addUserCount = () => {
    this.userCount += 1;
  }

  toggerLoading = (loading: boolean) => {
    this.loading = loading
  }

  search = (userInfo: UserInfo) => {
    rootStotes.count.add();
    this.searchId(userInfo.id);
    this.searchName(userInfo.name);
    this.searchPhone(userInfo.phone);
  }
  searchId = (id: number) => {
    if (!id) return;
    this.userInof = [...this.userInof.filter(item => item.id === +id)];
  }
  searchName = (name: string) => {
    if (!name) return
    this.userInof = [...this.userInof.filter(item => item.name.includes(name))];
  }
  searchPhone = (phone: string) => {
    if (!phone) return
    this.userInof = [...this.userInof.filter(item => item.phone === phone)];
  }
}

export const userStore = new UserStore();