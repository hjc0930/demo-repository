import { useState } from "react";

const role = {
  count: 1,
};

function useAuth() {
  const [count, setCount] = useState(role);

  return {
    count,
    setCount,
  };
}

useAuth.role = role;

export default useAuth;
