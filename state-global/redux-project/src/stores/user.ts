import { createSlice } from "@reduxjs/toolkit"
import { getUserList } from "../services/user";
import { UserInfo } from "../entitys/user";

export interface UserStore {
  userInof: UserInfo[];
  loading: boolean;
  rule: Record<string, 0 | 1>;
  userCount: number;

  // init: () => Promise<void>;
  // search: (userInfo: UserInfo) => void;
  // searchId: (id: number) => void;
  // searchName: (name: string) => void;
  // searchPhone: (phone: string) => void;
  // addUserCount: () => void;
}

const initialState: UserStore = {
  userInof: [],
  loading: true,
  rule: {},
  userCount: 0
}


// export const userSlice = createSlice({
//   name: "user",
//   initialState,
//   reducers: {
//     init: async (state) => {
//       state.loading = true;
//       const result = await getUserList();

//       state.userInof = result;
//       state.loading = false;

//     },
//   }
// })
