import { createRef } from "react";
import { MessageRef } from "./Message";
import { createRoot } from "react-dom/client";
import { render } from "react-dom";
import Message from "./Message";

const staticMessage = () => {
  const ref = createRef<MessageRef>();

  render(
    <Message ref={ref} content="Demo Static" />,
    document.querySelector(".message-container")!
  );

  return {
    remove: ref.current?.remove,
  };
};

export default staticMessage;
