import { PropsWithChildren, useEffect } from "react";
import useAppInitialzation from "@/stores/useAppInitialzation";

const AppInitialization = (props: PropsWithChildren) => {
  const initialzation = useAppInitialzation((state) => state.initialzation);
  const isFirstRender = useAppInitialzation((state) => state.isFirstRender);

  useEffect(() => {
    if (isFirstRender) {
      initialzation();
    }
  }, [isFirstRender]);

  return props.children;
};

export default AppInitialization;
