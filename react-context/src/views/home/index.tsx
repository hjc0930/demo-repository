import { Button } from "antd";
import { notification } from "antd";
import "./index.css";

const Home = () => {
  const [api, contextHolder] = notification.useNotification();

  const handleOpen = () => {};

  return (
    <>
      {contextHolder}
      <ul className="aaa-btn">
        <li>1</li>
        <li>2</li>
        <li>3</li>
      </ul>
      <Button type="primary" onClick={handleOpen}>
        Open
      </Button>
    </>
  );
};

export default Home;
