import { createRoot } from "react-dom/client";
import messageInstance from "../components/Message";

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
