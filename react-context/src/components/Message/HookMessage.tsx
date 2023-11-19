import { useEffect, useRef } from "react";
import Message from "./Message";
import type { MessageOptions } from ".";
import type { Flip } from "./flip";

interface HookMessageProps extends MessageOptions {
  comKey: string;
  flip: Flip;
  removeElements: (key: string) => void;
}

function HookMessage(props: HookMessageProps) {
  const { comKey, flip, removeElements } = props;
  const messageRef = useRef<HTMLDivElement>(null);

  const onLeavingAnimate = (
    keyframes: Keyframe[] | PropertyIndexedKeyframes | null,
    options?: number | KeyframeAnimationOptions | undefined
  ) =>
    new Promise((resolve, reject) => {
      messageRef.current
        ?.animate(keyframes, options)
        ?.finished.then(resolve, reject);
    });

  const onClose = () => {
    onLeavingAnimate(
      [
        {
          opacity: 0.8,
          transform: "translate(0, 0)",
        },
        {
          opacity: 0.5,
          transform: "translate(0, -5px)",
        },
        {
          opacity: 0,
          transform: "translate(0, -10px)",
        },
      ],
      {
        duration: 150,
        fill: "both",
      }
    )
    .then(() => {
      requestAnimationFrame(() => {
        removeElements(comKey);
        requestAnimationFrame(() => {
          flip.play(150);
        });
      });
    });
  };

  useEffect(() => {
    const duration = 3000 + 150 * Number(comKey.split("-").at(-1));
    const timer = setTimeout(onClose, duration);
    return () => {
      clearTimeout(timer);
    };
  }, []);

  return <Message ref={messageRef}>{comKey}</Message>;
}

export default HookMessage;
