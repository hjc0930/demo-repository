import { Button } from "antd";
import { notification } from "antd";

const Home = () => {
  const [api, contextHolder] = notification.useNotification();

  // const a: CSSMotionProps = {};

  const handleOpen = () => {
    api.open({
      message: "DemoDemoDemo",
      duration: 0,
      placement: "top",
    });

    // setTimeout(() => {
    //   console.log("timeout");

    //   result();
    // }, 3000);

    // console.log(remove);

    // msgApi.open({
    //   content: "Hellow",
    //   duration: 0,
    // });
    // messageApi
    //   .open({
    //     content: "Hellow",
    //     duration: 0,
    //   })
    //   .then((r) => {
    //     console.log(r);
    //   });
  };

  return (
    <>
      <Button onClick={handleOpen}>Open</Button>
      {/* {contextHolder} */}
      {contextHolder}
    </>
  );
};

export default Home;
