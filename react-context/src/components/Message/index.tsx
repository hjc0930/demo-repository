import { MessageProps } from "./types";
import useStore from "./useStore";

export const MessageProvider = (props: MessageProps) => {
  const { messageList, add, update, remove, clearAll } = useStore("top");

  return (
    <div>
      {messageList.top.map((item) => {
        return (
          <div
            style={{
              width: 100,
              lineHeight: "30px",
              border: "1px solid #000",
              margin: "20px",
            }}
          >
            {item.content}
          </div>
        );
      })}
    </div>
  );
};
