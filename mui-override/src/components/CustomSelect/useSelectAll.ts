import { useMemo, useCallback } from 'react';
import { SelectOption } from './types';

export interface UseSelectAllOptions<T> {
  options: SelectOption<T>[];
  value: T[];
  selectAll?: boolean;
  multiple?: boolean;
  onChange?: (event: React.SyntheticEvent, value: T[], reason: string) => void;
}

export interface UseSelectAllReturn<T> {
  optionsWithSelectAll: SelectOption<T | '__SELECT_ALL__'>[];
  isAllSelected: boolean;
  displayValue: string | null;
  handleSelectAll: (event: React.SyntheticEvent) => void;
}

const SELECT_ALL_VALUE = '__SELECT_ALL__' as const;

export function useSelectAll<T>({
  options,
  value,
  selectAll,
  multiple,
  onChange,
}: UseSelectAllOptions<T>): UseSelectAllReturn<T> {
  const enabledOptions = useMemo(
    () => options.filter((opt) => !opt.disabled),
    [options]
  );

  const isAllSelected = useMemo(() => {
    if (!multiple || !selectAll) return false;
    if (enabledOptions.length === 0) return false;
    return enabledOptions.every((opt) => value.includes(opt.value));
  }, [multiple, selectAll, enabledOptions, value]);

  const optionsWithSelectAll = useMemo(() => {
    if (!multiple || !selectAll) {
      return options as SelectOption<T | '__SELECT_ALL__'>[];
    }

    const selectAllOption: SelectOption<typeof SELECT_ALL_VALUE> = {
      value: SELECT_ALL_VALUE,
      label: isAllSelected ? 'Deselect all' : 'Select all',
    };

    return [selectAllOption, ...options] as SelectOption<T | '__SELECT_ALL__'>[];
  }, [multiple, selectAll, options, isAllSelected]);

  const displayValue = useMemo(() => {
    if (!multiple || !selectAll) return null;
    if (isAllSelected && enabledOptions.length > 1) {
      return 'all';
    }
    return null;
  }, [multiple, selectAll, isAllSelected, enabledOptions.length]);

  const handleSelectAll = useCallback(
    (event: React.SyntheticEvent) => {
      if (!multiple || !selectAll || !onChange) return;

      if (isAllSelected) {
        onChange(event, [], 'selectAll');
      } else {
        const allValues = enabledOptions.map((opt) => opt.value);
        onChange(event, allValues, 'selectAll');
      }
    },
    [multiple, selectAll, onChange, isAllSelected, enabledOptions]
  );

  return {
    optionsWithSelectAll,
    isAllSelected,
    displayValue,
    handleSelectAll,
  };
}

export { SELECT_ALL_VALUE };
