import React, { PropsWithChildren } from "react";
import { Root, createRoot } from "react-dom/client";
import "./index.css";

const MARK = "MESSAGE_ROOT";

type ContainerType = (Element | DocumentFragment) & {
  [MARK]?: Root;
};

function MessageContainer({ children }: PropsWithChildren) {
  return <div className="message-container">{children}</div>;
}

function Message() {
  return (
    <div className="message-wrapper">
      <div className="message-content">
        <p>This is a demo text</p>
      </div>
    </div>
  );
}

function render(node: React.ReactElement, container: ContainerType) {
  const root = container[MARK] || createRoot(container);

  console.log(container);

  root.render(node);

  container[MARK] = root;
}

export { Message, MessageContainer };
export default render;
