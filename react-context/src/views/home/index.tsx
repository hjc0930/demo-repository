import { Badge } from "antd";

const arr = [1, 2, 3, 4, 5, 6];
function Home() {
  return arr.map((item) => <p>{item}</p>);
}

export default Home;
