import { memo, useContext } from "react";
import { CurrentUserContext } from "../App";

const Child = memo(() => {
  const currentContext = useContext(CurrentUserContext);
  console.log(12323123);

  return <div>Child</div>;
});

export default Child;
