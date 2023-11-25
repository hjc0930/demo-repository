import messageInstance from "@/components/Message";
import { message } from "antd";

function MessageDemo() {
  const onOpen = () => {
    messageInstance({
      content: "",
    });
  };

  return (
    <div>
      <h2>Message</h2>
      <button onClick={onOpen}>Open</button>
    </div>
  );
}

export default MessageDemo;
