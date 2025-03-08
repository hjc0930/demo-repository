import { create } from "zustand";
import { getUser } from "@/services/user";
import useGlobalStore from "./useGlobalStore";

interface UseAppInitialzationType {
  isFirstRender: boolean;
  loading: boolean;
  initialzation: () => Promise<void>;
}

const useAppInitialzation = create<UseAppInitialzationType>((set) => ({
  loading: false,
  isFirstRender: true,
  initialzation: async () => {
    set({
      loading: true,
      isFirstRender: false,
    });
    try {
      const response = await getUser();
      useGlobalStore.setState({
        userInfo: response?.data,
      });
    } finally {
      set({
        loading: false,
      });
    }
  },
}));

export default useAppInitialzation;
