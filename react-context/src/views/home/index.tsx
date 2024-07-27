import { Button } from "antd";
import { notification } from "antd";
import "./index.css";

const Home = () => {
  const [api, contextHolder] = notification.useNotification();

  // const a: CSSMotionProps = {};

  const handleOpen = () => {};

  return (
    <>
      <ul className="aaa-btn">
        <li>1</li>
        <li>2</li>
        <li>3</li>
      </ul>
      <Button onClick={handleOpen}>Open</Button>
      {/* {contextHolder} */}
      {contextHolder}
    </>
  );
};

export default Home;
