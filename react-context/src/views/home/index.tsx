import { Button } from "antd";
import useMessage from "@/components/Message/useMessage";
import useAntdMessage from "antd/es/message/useMessage";

const Home = () => {
  const [messageApi, contextHolder] = useMessage();
  const [msgApi, ctxHolder] = useAntdMessage();

  const handleOpen = () => {
    // msgApi.open({
    //   content: "Hellow",
    //   duration: 0,
    // });
    messageApi
      .open({
        content: "Hellow",
        duration: 0,
      })
      .then((r) => {
        console.log(r);
      });
  };

  return (
    <>
      <Button onClick={handleOpen}>Open</Button>
      {contextHolder}
      {ctxHolder}
    </>
  );
};

export default Home;
