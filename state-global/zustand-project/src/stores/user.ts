import { create } from "zustand";
import type { UserInfo } from "../entitys/user";
import { getUserList } from "../services/user";
import { useCountStore } from "./count";

export interface UserStore {
  userInof: UserInfo[];
  loading: boolean;
  rule: Record<string, 0 | 1>;
  userCount: number;

  init: () => Promise<void>;
  search: (userInfo: UserInfo) => void;
  searchId: (id: number) => void;
  searchName: (name: string) => void;
  searchPhone: (phone: string) => void;
  addUserCount: () => void;
}

export const useUserStore = create<UserStore>((set, get) => ({
  userInof: [],
  loading: true,
  rule: {},
  userCount: 0,

  addUserCount: () => set(({ userCount }) => ({ userCount: userCount + 1 })),

  init: async () => {
    set(({ loading: true }));
    const result = await getUserList();
    const { count } = useCountStore.getState();

    set({ userInof: result });
    set(({ loading: false }))
  },
  search: (userInfo: UserInfo) => {
    const { searchId, searchName, searchPhone } = get();

    useCountStore.setState(({ count }) => ({ count: count + 1 }))
    searchId(userInfo.id);
    searchName(userInfo.name);
    searchPhone(userInfo.phone);
  },
  searchId: (id: number) => {
    if (!id) return;
    set(({ userInof }) => ({ userInof: [...userInof.filter(item => item.id === +id)] }))
  },
  searchName: (name: string) => {
    if (!name) return
    set(({ userInof }) => ({ userInof: userInof.filter(item => item.name.includes(name)) }))
  },
  searchPhone: (phone: string) => {
    if (!phone) return

    set(({ userInof }) => ({ userInof: userInof.filter(item => item.phone === phone) }))
  },
}))


export const mailSelector = (store: UserStore) => {
  return store.userInof.map(item => item.name + "@ssga.com")
}