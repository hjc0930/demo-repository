import useModel from "../stores/useModel";
import counterStore from "../stores/counter";
import { useContext } from "react";
import { ThemeContext } from "../App";

const Home = () => {
  const theme = useContext(ThemeContext);

  return <div>{theme}</div>;
};

export default Home;
