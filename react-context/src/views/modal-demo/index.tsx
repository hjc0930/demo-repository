import { Button } from "@mui/material";
import { useState } from "react";
import Modal, { useModal } from "@/components";
import { useAppStore, useAppDispatch } from "@/store/AppStore";

const Child = () => {
  const store = useAppStore();
  const dispatch = useAppDispatch();
  console.log(store);

  return (
    <div>
      <h2>Child</h2>
      <p>{store.count}</p>
      <button
        onClick={() => {
          dispatch({
            count: 3,
          });
        }}
      >
        Add
      </button>
    </div>
  );
};

function ModalDemo() {
  const [open, setOpen] = useState(false);
  const [modal, contextHolder] = useModal();
  const onOpen = () => {
    setOpen(true);
  };
  const onClose = () => {
    setOpen(false);
  };

  const onOpenConfirm = () => {
    modal.confirm();
  };

  return (
    <>
      {contextHolder}
      <Button onClick={onOpen} variant="outlined">
        Open
      </Button>
      <Button onClick={onOpenConfirm} variant="outlined">
        Open Confirm
      </Button>
      <Modal title="Adadsd" open={open} onClose={onClose}>
        <Child />
      </Modal>
    </>
  );
}

export default ModalDemo;
