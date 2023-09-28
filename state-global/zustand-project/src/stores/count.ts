import { create } from "zustand"

const useCountStore = create(() => ({
  count: 0
}))

export { useCountStore };