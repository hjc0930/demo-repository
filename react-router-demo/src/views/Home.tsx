import { useRequest } from "ahooks";
import { Button } from "antd";

const Home = () => {
  const { loading } = useRequest(
    async () => {
      return {
        url: "/api/user",
        method: "GET",
      };
    },
    {
      manual: true,
    }
  );

  return (
    <div>
      <Button loading={loading}>Close</Button>
    </div>
  );
};

export default Home;
