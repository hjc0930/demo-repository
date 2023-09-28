import { MyContext } from "./Context";
import React from "react";

// 封装代码以复用
const useStores = () => React.useContext(MyContext);

export default useStores;
