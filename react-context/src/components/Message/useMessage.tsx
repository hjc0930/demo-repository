import { ReactNode, useRef, useState } from "react";
import {
  MessageEnum,
  MessageMethods,
  MessageProps,
  MessageReturn,
  MessageType,
  UseMessageReturn,
} from "./types";
import Message from "./Message";
import { createPortal } from "react-dom";

const useMessage = (): UseMessageReturn => {
  const uuid = useRef(0);
  const [messageList, setMessageList] = useState<MessageProps[]>([]);

  const contextHolder = messageList.length
    ? createPortal(
        <div className="message-container-wrapper">
          {messageList.map((item) => (
            <Message {...item} key={item.id} />
          ))}
        </div>,
        document.body
      )
    : null;

  const _getId = (config: MessageProps) => {
    return config.id || uuid.current++;
  };

  const clear = () => {
    setMessageList([]);
  };
  const remove = (id: React.Key) => {
    setMessageList((prev) => prev.filter((item) => item.id !== id));
  };

  const open = (config: MessageProps) => {
    let onResolve: any;
    const id = _getId(config);

    // const duration = mergeConfig.duration;
    // const timer =
    //   duration !== 0
    //     ? setTimeout(() => {
    //         onResolve?.(mergeConfig);
    //         remove(id);
    //       }, duration)
    //     : null;

    const inlineRemove = () => {
      // timer && clearTimeout(timer);
      remove(id);
    };
    inlineRemove.then = (onFulfilled: (config: MessageProps) => void) => {
      onResolve = onFulfilled;
    };

    const mergeConfig: MessageProps & { onResolve: () => void } = {
      ...config,
      type: config.type ?? "info",
      duration: config.duration ?? 3000,
      id,
      onResolve,
    };

    setMessageList((prev) => [...prev, mergeConfig]);

    return inlineRemove as MessageReturn;
  };

  const simpleMethods: any = [...Object.keys(MessageEnum)].reduce(
    (prev, type: any) => {
      return {
        ...prev,
        [type]: (content: ReactNode, config: Omit<MessageProps, "content">) => {
          return open({
            ...config,
            type,
            content,
          });
        },
      };
    },
    {}
  );

  return [
    {
      open,
      remove,
      clear,
      ...simpleMethods,
    },
    contextHolder,
  ];
};

export default useMessage;
