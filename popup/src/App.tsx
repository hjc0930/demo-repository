import { useState } from "react";
import {
  useInteractions,
  useFloating,
  useHover,
  useDismiss,
  useClick,
  flip,
} from "@floating-ui/react";

function App() {
  const [isOpen, setIsOpen] = useState(false);

  const { refs, floatingStyles, context } = useFloating({
    open: isOpen,
    onOpenChange: setIsOpen,
    middleware: [flip()],
  });

  const click = useClick(context);
  const hover = useHover(context);
  const dismiss = useDismiss(context);

  const { getReferenceProps, getFloatingProps } = useInteractions([
    hover,
    click,
    dismiss,
  ]);

  return (
    <>
      <button ref={refs.setReference} {...getReferenceProps()}>
        hello
      </button>
      {isOpen && (
        <div
          ref={refs.setFloating}
          style={floatingStyles}
          {...getFloatingProps()}
        >
          光光光光光
        </div>
      )}
    </>
  );
}

export default App;
