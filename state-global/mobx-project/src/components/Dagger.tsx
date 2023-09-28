import { Button, Drawer } from "antd";
import { observer } from "mobx-react-lite";
import useGlobalStore from "../hooks/useGlobalStore";

const DrawerCom = (props: { open?: boolean; onClose?: () => void }) => {
  const { userCount, addUserCount } = useGlobalStore("user");

  return (
    <Drawer open={props?.open} onClose={props?.onClose} getContainer={false}>
      <h4>userCount: {userCount}</h4>
      <Button onClick={addUserCount}>Add</Button>
    </Drawer>
  );
};

export default observer(DrawerCom);
