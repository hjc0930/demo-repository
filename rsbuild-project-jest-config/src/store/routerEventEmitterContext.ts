import { createContext, useContext } from "react";
import { EventEmitter } from "../hook/useEventEmitter";

const RouterEventEmitterContext = createContext<EventEmitter>(
  new EventEmitter()
);

const RouterEventEmitterProvider = RouterEventEmitterContext.Provider;
const RouterEventEmitterConsumer = RouterEventEmitterContext.Consumer;
const useRouterEventEmitterContext = () =>
  useContext(RouterEventEmitterContext);

export {
  RouterEventEmitterContext,
  RouterEventEmitterProvider,
  RouterEventEmitterConsumer,
  useRouterEventEmitterContext,
};
