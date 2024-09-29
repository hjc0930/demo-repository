import { Modal, ModalProps } from "antd";
import { ReactNode, useState } from "react";

interface ConfirmProps {
  title?: ReactNode;
  content?: ReactNode;
  onOk?: () => void;
  onCancel?: () => void;
}

const useConfirm = () => {
  const [propsState, setPropsState] = useState<ModalProps>({});

  const confirm = (props: ConfirmProps) => {
    const { content, ...restProps } = props;
    const onOk = () => {
      props.onOk?.();
      setPropsState({});
    };
    const onCancel = () => {
      props?.onCancel?.();
      setPropsState({});
    };

    setPropsState({
      ...restProps,
      open: true,
      children: content,
      onOk,
      onCancel,
    });
  };
  const context = <Modal {...propsState} destroyOnClose />;

  return [confirm, context] as const;
};

export default useConfirm;
