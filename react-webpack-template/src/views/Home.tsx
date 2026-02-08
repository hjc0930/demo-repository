import React, { useEffect } from "react";
import { observer } from "mobx-react-lite";
import { useRootStore } from "@/hooks";

const Home: React.FC = () => {
  const posts = useRootStore("posts");
  const users = useRootStore("user");

  useEffect(() => {
    users.init();
    console.log(posts.getUserInfo());
  }, []);

  return (
    <div>
      <h1>{posts.count}</h1>
      <ul>
        {users.userInfo.map((item) => (
          <li key={item.id}>{item.name}</li>
        ))}
      </ul>
      <button onClick={posts.add}>Add</button>
    </div>
  );
};

export default observer(Home);
