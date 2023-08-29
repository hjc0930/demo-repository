import { createContext } from "react";
import { Posts } from "./posts";
import { User } from "./user";

const RootStore = {
  user: new User(),
  posts: new Posts(),
};

const StoreContext = createContext<typeof RootStore | null>(null);

export { RootStore, StoreContext };
