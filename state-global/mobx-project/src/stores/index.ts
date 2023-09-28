import { createContext } from "react";
import { countStore } from "./count";
import { userStore } from "./user";


const rootStotes = {
  count: countStore,
  user: userStore
}

export type RootStoresType = typeof rootStotes;


export const RootStoresContext = createContext(rootStotes);
export default rootStotes