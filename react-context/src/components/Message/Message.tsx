import {
  ForwardRefRenderFunction,
  forwardRef,
  useEffect,
  useImperativeHandle,
} from "react";
import "./index.css";
import { MessageProps } from "./types";

export interface MessageRef {
  remove: () => void;
}

const Message: ForwardRefRenderFunction<MessageRef, MessageProps> = (
  props,
  ref
) => {
  const { type, content } = props;

  useImperativeHandle(ref, () => ({
    remove: () => {
      console.log(12213);
    },
  }));

  return (
    <div className="message-container">
      <div className="message-wrapper">
        {/* <span className="message-icon">{icon}</span> */}
        <span className="message-icon">{type}</span> -
        <span className="message-content">{content}</span>
      </div>
    </div>
  );
};

export default forwardRef(Message);
