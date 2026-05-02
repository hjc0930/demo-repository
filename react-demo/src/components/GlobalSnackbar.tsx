import { useEffect, useCallback } from "react";
import { Alert, Snackbar } from "@mui/material";
import { useStore } from "@tanstack/react-store";
import { snackbarStore } from "@/store/useSnackbarStore";

const GlobalSnackbar = () => {
  const errors = useStore(snackbarStore, (s) => s.errors);

  const removeError = useCallback(
    (id: string) => snackbarStore.state.removeError(id),
    [],
  );

  return (
    <>
      {errors.map((error, index) => (
        <SnackbarItem
          key={error.id}
          id={error.id}
          message={error.message}
          index={index}
          onClose={removeError}
        />
      ))}
    </>
  );
};

interface SnackbarItemProps {
  id: string;
  message: string;
  index: number;
  onClose: (id: string) => void;
}

const SnackbarItem = ({ id, message, index, onClose }: SnackbarItemProps) => {
  useEffect(() => {
    const timer = setTimeout(() => onClose(id), 3000);
    return () => clearTimeout(timer);
  }, [id, onClose]);

  return (
    <Snackbar
      open
      anchorOrigin={{ vertical: "top", horizontal: "center" }}
      sx={{ top: `${64 + index * 60}px !important` }}
    >
      <Alert severity="error" onClose={() => onClose(id)}>
        {message}
      </Alert>
    </Snackbar>
  );
};

export default GlobalSnackbar;
