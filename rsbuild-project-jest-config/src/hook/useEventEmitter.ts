import { useEffect, useRef } from "react";

type EventListener = (...args: any[]) => void;

class EventEmitter {
  private subscriptions: Set<EventListener> = new Set();

  public emit = (...args: any[]) => {
    this.subscriptions.forEach((subscription) => {
      subscription(...args);
    });
  };

  public useSubscription = (listener: EventListener) => {
    const callbackRef = useRef<EventListener>(null);
    callbackRef.current = listener;

    useEffect(() => {
      const subscription = (...args: any[]) => {
        if (callbackRef.current) {
          callbackRef.current(...args);
        }
      };
      this.subscriptions.add(subscription);
      return () => {
        this.subscriptions.delete(subscription);
      };
    }, []);
  };
}

const useEventEmitter = () => {
  const eventEmitterRef = useRef<EventEmitter>(null);

  if (!eventEmitterRef.current) {
    eventEmitterRef.current = new EventEmitter();
  }

  return eventEmitterRef.current;
};

export { EventEmitter };
export default useEventEmitter;
