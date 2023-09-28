import { UserInfo } from "../entitys/user";

export const getUserList = (): Promise<UserInfo[]> => new Promise(resolve => {
  setTimeout(() => {
    resolve([
      {
        id: 0,
        name: "xiaoming",
        mail: "xiaoming@ss.com",
        phone: "123xxx"
      },
      {
        id: 1,
        name: "xiaoming2",
        mail: "xiaoming2@ss.com",
        phone: "123xxx"
      },
      {
        id: 2,
        name: "xiaoming3",
        mail: "xiaoming2@ss.com",
        phone: "1234xxx"
      },
    ])
  }, 800)
})