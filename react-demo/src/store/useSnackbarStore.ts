import { createStore } from "@tanstack/react-store";

interface SnackbarError {
  id: string;
  message: string;
}

interface SnackbarState {
  errors: SnackbarError[];
  addError: (message: string) => void;
  removeError: (id: string) => void;
  clearAll: () => void;
}

export const snackbarStore = createStore<SnackbarState>({
  errors: [],

  addError(message) {
    const id = crypto.randomUUID();
    const next = [...snackbarStore.state.errors, { id, message }];
    snackbarStore.setState((prev) => ({
      ...prev,
      errors: next.length > 3 ? next.slice(-3) : next,
    }));
  },

  removeError(id) {
    snackbarStore.setState((prev) => ({
      ...prev,
      errors: prev.errors.filter((e) => e.id !== id),
    }));
  },

  clearAll() {
    snackbarStore.setState((prev) => ({ ...prev, errors: [] }));
  },
});
