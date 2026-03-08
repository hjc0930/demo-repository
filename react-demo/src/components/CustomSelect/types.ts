import type { AutocompleteProps } from '@mui/material';

export interface SelectOption<T = string> {
  value: T;
  label: string;
  disabled?: boolean;
}

export type SelectValue<T, Multiple extends boolean | undefined = undefined> = Multiple extends true
  ? T[]
  : T | null;

export type ChangeReason = 'createOption' | 'selectOption' | 'removeOption' | 'clear' | 'selectAll';
export type CloseReason = 'createOption' | 'selectOption' | 'removeOption' | 'toggleInput' | 'escape' | 'blur';

export interface CustomSelectProps<T>
  extends Omit<
    AutocompleteProps<SelectOption<T>, boolean, boolean, boolean>,
    'options' | 'value' | 'onChange' | 'multiple' | 'isOptionEqualToValue' | 'getOptionLabel'
  > {
  options: SelectOption<T>[];
  value?: T | T[] | null;
  onChange?: (
    event: React.SyntheticEvent,
    value: T | T[] | null,
    reason: ChangeReason
  ) => void;

  multiple?: boolean;
  searchable?: boolean;
  selectAll?: boolean;
  autoCompleted?: boolean;

  label?: string;
  placeholder?: string;
  error?: boolean;
  helperText?: string;
}
