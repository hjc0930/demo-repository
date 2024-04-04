import { useEffect } from "react";
import yayJpg from "../assets/yay.jpg";

export default function HomePage() {
  const init = () => {
    try {
      fetch("http://localhost:3000");
    } catch (error) {}
  };
  useEffect(() => {
    init();
  }, []);
  return (
    <div>
      <h2>Yay! Welcome to umi!</h2>
      <p>
        <img src={yayJpg} width="388" />
      </p>
      <p>
        To get started, edit <code>pages/index.tsx</code> and save to reload.
      </p>
    </div>
  );
}
