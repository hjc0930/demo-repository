import { Root, createRoot } from "react-dom/client";
import render, { MessageContainer, Message } from "./createMessage";
import React, { useEffect, useState } from "react";

interface MessageOptions {
  content: string;
  icon?: React.ReactNode;
  duration?: number;
  type?: "info" | "success" | "warning" | "error";
}

const methods = ["success", "info", "error", "warning"] as const;
const elements: React.ReactElement[] = [];
let uuid = 0;
let root: Root;

function HookMessage(props: MessageOptions) {
  useEffect(() => {
    setTimeout(() => {
      elements.pop();
      props?.render();
      console.log(elements);
    }, 2000);
  }, []);
  return <Message />;
}

function messageInstance(options: MessageOptions) {
  let messageElementContainer = document.querySelector(".message-container");
  if (!messageElementContainer) {
    messageElementContainer = document.createElement("div");
    messageElementContainer.classList.add("message-container");

    document.body.appendChild(messageElementContainer);
    root = createRoot(messageElementContainer);
  }
  const render = () =>
    root.render(<MessageContainer>{elements}</MessageContainer>);
  render();
  elements.push(
    <HookMessage {...options} render={render} key={`message-${uuid++}`} />
  );
}

export default messageInstance;
