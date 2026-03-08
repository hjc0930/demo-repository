import { useEffect, useRef, useCallback } from 'react';
import { SelectOption } from './types';

export interface UseAutoCompletedOptions<T> {
  filteredOptions: SelectOption<T>[];
  value: T | T[] | null | undefined;
  multiple?: boolean;
  autoCompleted?: boolean;
  open?: boolean;
  onChange?: (event: React.SyntheticEvent, value: T | T[], reason: string) => void;
  setInputValue?: (value: string) => void;
}

export function useAutoCompleted<T>({
  filteredOptions,
  value,
  multiple,
  autoCompleted,
  open,
  onChange,
  setInputValue,
}: UseAutoCompletedOptions<T>) {
  const hasAutoCompletedRef = useRef(false);
  const prevOpenRef = useRef(open);

  useEffect(() => {
    if (!autoCompleted || multiple) return;

    if (open && !prevOpenRef.current) {
      hasAutoCompletedRef.current = false;
    }
    prevOpenRef.current = open;

    if (
      !open &&
      filteredOptions.length === 1 &&
      !hasAutoCompletedRef.current
    ) {
      const singleOption = filteredOptions[0];
      const currentValue = value as T | null | undefined;

      if (currentValue !== singleOption.value && !singleOption.disabled) {
        hasAutoCompletedRef.current = true;

        if (onChange) {
          const syntheticEvent = {} as React.SyntheticEvent;
          onChange(syntheticEvent, singleOption.value, 'autoCompleted');
        }

        if (setInputValue) {
          setInputValue(singleOption.label);
        }
      }
    }
  }, [
    open,
    filteredOptions,
    value,
    multiple,
    autoCompleted,
    onChange,
    setInputValue,
  ]);

  const resetAutoCompleted = useCallback(() => {
    hasAutoCompletedRef.current = false;
  }, []);

  return {
    resetAutoCompleted,
  };
}
