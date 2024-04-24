import styled from "@emotion/styled";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { PropsWithChildren, ReactNode } from "react";

interface RenderFooterType {
  onOk?: () => void;
  onClose?: () => void;
}

export interface ModalProps extends PropsWithChildren {
  open?: boolean;
  title?: React.ReactNode;
  width?: number;
  renderFooter?: (params: RenderFooterType) => ReactNode;
  onOk?: () => void;
  onClose?: () => void;
}

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  "& .MuiPaper-root": {
    width: "var(--dialog-width)",
  },
}));

const Modal = (props: ModalProps) => {
  const {
    open = false,
    title,
    width = 520,
    children,
    renderFooter,
    onOk,
    onClose,
  } = props;

  const Footer =
    typeof renderFooter === "function"
      ? renderFooter({ onOk, onClose })
      : [
          <Button onClick={onClose} key="cancel">
            Cancel
          </Button>,
          <Button onClick={onOk} key="ok">
            Subscribe
          </Button>,
        ];

  return (
    <BootstrapDialog
      open={open}
      onClose={onClose}
      fullWidth={true}
      maxWidth={false}
      style={
        {
          "--dialog-width": `${width}px`,
        } as any
      }
    >
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>{children}</DialogContent>
      <DialogActions>{Footer}</DialogActions>
    </BootstrapDialog>
  );
};

export default Modal;
