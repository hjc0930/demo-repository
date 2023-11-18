import { Root, createRoot } from "react-dom/client";
import render, { MessageContainer, Message } from "./createMessage";
import React, { useEffect, useRef, useState } from "react";

interface MessageOptions {
  content: string;
  icon?: React.ReactNode;
  duration?: number;
  type?: "info" | "success" | "warning" | "error";
}

const methods = ["success", "info", "error", "warning"] as const;
const elements: Map<string, React.ReactElement> = new Map();

let uuid = 0;
let root: Root;

function HookMessage(props: MessageOptions) {
  const messageRef = useRef<HTMLDListElement>(null);
  useEffect(() => {
    let delayTimer = NaN;
    const timer = setTimeout(() => {
      messageRef.current?.animate(
        [
          {
            opacity: 1,
            transform: "translate(0, 0)",
          },
          {
            opacity: 0,
            transform: "translate(0, -30px)",
          },
        ],
        {
          duration: 200,
          fill: "both",
        }
      );
      delayTimer = setTimeout(() => {
        elements.delete(props.comKey);
        props?.render();
      }, 200);
    }, 2000);
    return () => {
      clearTimeout(timer);
      if (!Number.isNaN(delayTimer)) {
        clearTimeout(delayTimer);
      }
    };
  }, []);
  return <Message ref={messageRef} />;
}

function messageInstance(options: MessageOptions) {
  let messageElementContainer = document.querySelector(".message-container");
  if (!messageElementContainer) {
    messageElementContainer = document.createElement("div");
    messageElementContainer.classList.add("message-container");

    document.body.appendChild(messageElementContainer);
    root = createRoot(messageElementContainer);
  }
  const key = `message-${uuid++}`;
  const render = () => {
    root.render(<MessageContainer>{[...elements.values()]}</MessageContainer>);
  };
  elements.set(
    key,
    <HookMessage {...options} render={render} key={key} comKey={key} />
  );
  render();
}

export default messageInstance;
