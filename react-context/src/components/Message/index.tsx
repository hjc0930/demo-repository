import { Root, createRoot } from "react-dom/client";
import React from "react";
import HookMessage from "./HookMessage";
import { Flip } from "./flip";

export interface MessageOptions {
  content: string;
  icon?: React.ReactNode;
  duration?: number;
  type?: "info" | "success" | "warning" | "error";
}

let uuid = 0;
let root: Root;
let messageElementContainer: HTMLDivElement | null = null;
let flip: Flip;
const elements: Map<string, React.ReactElement> = new Map();
const methods = ["success", "info", "error", "warning"] as const;

const render = () => {
  root.render([...elements.values()]);
};

const removeElements = (key: string) => {
  uuid--;
  elements.delete(key);
  render();
};

function messageInstance(options: MessageOptions) {
  if (!messageElementContainer) {
    messageElementContainer = document.createElement("div");
    flip = new Flip(messageElementContainer);
    messageElementContainer.classList.add("message-container");

    document.body.appendChild(messageElementContainer);
    root = createRoot(messageElementContainer);
  }
  const key = `message-${uuid++}`;
  elements.set(
    key,
    <HookMessage
      {...options}
      key={key}
      flip={flip}
      comKey={key}
      removeElements={removeElements}
    />
  );
  render();

  requestAnimationFrame(() => {
    flip.startRectOfrecalculate();
  });
}

export default messageInstance;
