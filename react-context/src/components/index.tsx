import React, { memo, forwardRef, useImperativeHandle } from "react";

import Modal from "./Modal";
import { useRef, useState } from "react";

interface ElementsHolderRef {
  patchElement: (element: React.ReactNode) => void;
}

let uuid = 0;

const ElementHolder = memo(
  forwardRef<ElementsHolderRef>((_, ref) => {
    const [elements, setElements] = useState<React.ReactNode[]>([]);

    const patchElement = (element: React.ReactNode) => {
      setElements((elements) => [...elements, element]);
    };

    useImperativeHandle(ref, () => ({
      patchElement,
    }));

    return <>{elements}</>;
  })
);

function useModal(): [
  instance: {
    confirm: () => void;
  },
  contextHolder: React.ReactElement
] {
  const elementHolderRef = useRef<ElementsHolderRef>(null);

  const confirm = () => {
    const hook = (
      <Modal key={`modal-${uuid++}`} open>
        12312312
      </Modal>
    );

    elementHolderRef.current?.patchElement(hook);
  };

  const modal = {
    confirm,
  };

  return [modal, <ElementHolder key="modal-holder" ref={elementHolderRef} />];
}

export { useModal };

export default Modal;
