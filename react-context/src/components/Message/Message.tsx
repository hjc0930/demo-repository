import {
  PropsWithChildren,
  forwardRef,
  useImperativeHandle,
  useRef,
} from "react";
import "./index.css";

const Message = forwardRef<any, PropsWithChildren>((props, ref) => {
  const messageRef = useRef(null);

  useImperativeHandle(ref, () => messageRef.current);

  return (
    <div className="message-wrapper" ref={messageRef}>
      <div className="message-content">
        <p>This is {props.children} demo text</p>
      </div>
    </div>
  );
});

export default Message;
