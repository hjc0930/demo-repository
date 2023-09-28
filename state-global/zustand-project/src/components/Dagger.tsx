import { Button, Drawer } from "antd";
import { useUserStore } from "../stores/user";
import { shallow } from "zustand/shallow";
import { memo } from "react";

const DrawerCom = (props: { open?: boolean; onClose?: () => void }) => {
  const { userCount, addUserCount } = useUserStore(
    (store) => ({
      userCount: store.userCount,
      addUserCount: store.addUserCount,
    }),
    shallow
  );
  return (
    <Drawer open={props?.open} onClose={props?.onClose}>
      <h4>userCount: {userCount}</h4>
      <Button onClick={addUserCount}>Add</Button>
    </Drawer>
  );
};

export default memo(DrawerCom);
