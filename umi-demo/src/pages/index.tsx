import { useEffect } from "react";
import yayJpg from "../assets/yay.jpg";
import { Pagination } from "antd";

export default function HomePage() {
  return (
    <div>
      <h2>Yay! Welcome to umi!</h2>
      <p>
        <img src={yayJpg} width="388" />
      </p>
    </div>
  );
}
