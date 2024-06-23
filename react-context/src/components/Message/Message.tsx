import { useEffect } from "react";
import "./index.css";
import { MessageProps } from "./types";

const Message = (props: MessageProps) => {
  const { type, content } = props;

  useEffect(() => {
    // setTimeout(() => {
    //   console.log(onResolve);
    // }, 3000);
    return () => {
      console.log(123123);
    };
  }, []);

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

export default Message;
