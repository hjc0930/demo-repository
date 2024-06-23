import { ReactNode } from "react";

export enum MessageEnum {
  info = "info",
  success = "success",
  warning = "warning",
  error = "error",
  loading = "loading",
}

type MessageMethod = (
  content: ReactNode,
  config?: Omit<MessageProps, "content">
) => MessageReturn;

export interface MessageMethods {
  info: MessageMethod;
  success: MessageMethod;
  warning: MessageMethod;
  error: MessageMethod;
  loading: MessageMethod;
}

export type MessageType = keyof typeof MessageEnum;

export interface MessageProps {
  id?: React.Key;
  type?: MessageType;
  duration?: number;
  icon?: ReactNode;
  content?: ReactNode;
}
export interface MessageReturn extends PromiseLike<MessageProps> {
  (): void;
}

export type UseMessageReturn = [
  {
    open(config: MessageProps): MessageReturn;
    remove(id: number): void;
    clear(): void;
  } & MessageMethods,
  React.ReactPortal | null
];
