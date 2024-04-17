import { useState } from "react";

const useControlledValue = <T = any>(props?: {
  value?: T;
  defaultValue?: T;
  onChange?: (newValue: T) => void;
}) => {
  const { value, defaultValue, onChange } = props || {};
  const [internalValue, setInternalValue] = useState<T | undefined>(
    value || defaultValue
  );

  const setValue = (newValue?: React.SetStateAction<T | undefined>) => {
    const val =
      typeof newValue === "function"
        ? (newValue as Function)?.(internalValue)
        : newValue;
    if (onChange) {
      onChange(val);
    } else {
      setInternalValue(val);
    }
  };

  return [value !== undefined ? value : internalValue, setValue] as const;
};

export default useControlledValue;
