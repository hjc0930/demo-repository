import { createContext, useReducer } from "react";

// 纯函数reducer
const reducer = (state: any, action: any) => {
  // action包括 具体的类型type，
  // 除了`type`之外，action 对象的结构其实完全取决于你自己。
  // 这里使用了payload代表dipatch传过来的数据
  switch (action.type) {
    case "list":
      return { ...state, list: action.payload };
    case "data":
      return { ...state, data: action.payload };
    case "time":
      return { ...state, time: action.payload };
    default:
      return state;
  }
};
const list = [
  { num: 0, key: 0 },
  { num: 1, key: 1 },
  { num: 2, key: 2 },
];
export const MyContext = createContext<any>({
  list: [],
  data: null,
  time: Date.now(),
});

function ContextProvider({ children }: any) {
  const [state, dispatch] = useReducer(reducer, {
    list,
    data: null,
    time: Date.now(),
  });

  const value = {
    state,
    dispatch,
  };
  return <MyContext.Provider value={value as any}>{children}</MyContext.Provider>;
}

export default ContextProvider;
