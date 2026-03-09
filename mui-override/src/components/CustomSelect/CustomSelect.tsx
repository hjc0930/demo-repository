import React, { useState, useMemo, useCallback } from 'react';
import {
  Autocomplete,
  TextField,
  Chip,
  Checkbox,
  FormControl,
  FormHelperText,
  autocompleteClasses,
} from '@mui/material';
import { useTheme } from '@mui/material/styles';
import CheckBoxOutlineBlankIcon from '@mui/icons-material/CheckBoxOutlineBlank';
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import {
  CustomSelectProps,
  SelectOption,
  ChangeReason,
} from './types';
import { useSelectAll, SELECT_ALL_VALUE } from './useSelectAll';
import { useAutoCompleted } from './useAutoCompleted';

const icon = <CheckBoxOutlineBlankIcon fontSize="small" />;
const checkedIcon = <CheckBoxIcon fontSize="small" />;

function isSelectAllOption<T>(value: T | '__SELECT_ALL__'): value is '__SELECT_ALL__' {
  return value === SELECT_ALL_VALUE;
}

export function CustomSelect<T>(props: CustomSelectProps<T>) {
  const {
    options,
    value,
    onChange,
    multiple = false,
    searchable = false,
    selectAll = false,
    autoCompleted = false,
    label,
    placeholder,
    disabled = false,
    error = false,
    helperText,
    size = 'small',
    ...restProps
  } = props;

  const theme = useTheme();
  const [inputValue, setInputValue] = useState<string>('');
  const [open, setOpen] = useState(false);

  const multipleValue = useMemo(() => {
    if (multiple && Array.isArray(value)) {
      return value as T[];
    }
    return [];
  }, [multiple, value]);

  const singleValue = useMemo(() => {
    if (!multiple && !Array.isArray(value)) {
      return value as T | null;
    }
    return null;
  }, [multiple, value]);

  const {
    optionsWithSelectAll,
    isAllSelected,
    displayValue,
    handleSelectAll,
  } = useSelectAll<T>({
    options,
    value: multipleValue,
    selectAll: multiple && selectAll,
    multiple,
    onChange: (event, newValue, reason) => {
      if (onChange) {
        onChange(event, newValue, reason as ChangeReason);
      }
    },
  });

  const filteredOptions = useMemo(() => {
    if (!searchable || !inputValue) {
      return options;
    }
    return options.filter((option) =>
      option.label.toLowerCase().includes(inputValue.toLowerCase())
    );
  }, [options, searchable, inputValue]);

  useAutoCompleted<T>({
    filteredOptions,
    value: multiple ? multipleValue : singleValue,
    multiple,
    autoCompleted,
    open,
    onChange: (event, newValue, reason) => {
      if (onChange) {
        onChange(event, newValue, reason as ChangeReason);
      }
    },
    setInputValue,
  });

  const selectedOptions = useMemo(() => {
    if (multiple) {
      return options.filter((opt) => multipleValue.includes(opt.value));
    }
    const single = options.find((opt) => opt.value === singleValue);
    return single ? [single] : [];
  }, [multiple, multipleValue, singleValue, options]);

  // Handle change for multiple mode
  const handleMultipleChange = useCallback(
    (
      event: React.SyntheticEvent,
      newValue: (SelectOption<T> | SelectOption<'__SELECT_ALL__'>)[]
    ) => {
      const selectAllOption = newValue.find((opt) => isSelectAllOption(opt.value));
      if (selectAllOption) {
        handleSelectAll(event);
        return;
      }

      const actualValues = newValue
        .filter((opt): opt is SelectOption<T> => !isSelectAllOption(opt.value))
        .map((opt) => opt.value);

      if (onChange) {
        onChange(event, actualValues, 'selectOption');
      }
    },
    [onChange, handleSelectAll]
  );

  // Handle change for single mode
  const handleSingleChange = useCallback(
    (
      event: React.SyntheticEvent,
      newValue: SelectOption<T> | SelectOption<'__SELECT_ALL__'> | null
    ) => {
      if (newValue && isSelectAllOption(newValue.value)) {
        // In single mode, select all doesn't make sense, just ignore
        return;
      }

      const selectedValue = newValue ? (newValue as SelectOption<T>).value : null;
      if (onChange) {
        onChange(event, selectedValue, 'selectOption');
      }
    },
    [onChange]
  );

  const handleChange = multiple ? handleMultipleChange : handleSingleChange;

  const getOptionLabel = useCallback(
    (option: SelectOption<T> | SelectOption<'__SELECT_ALL__'>) => {
      return option.label;
    },
    []
  );

  const isOptionEqualToValue = useCallback(
    (
      option: SelectOption<T> | SelectOption<'__SELECT_ALL__'>,
      valueOption: SelectOption<T> | SelectOption<'__SELECT_ALL__'>
    ) => {
      return option.value === valueOption.value;
    },
    []
  );

  const renderOption = useCallback(
    (
      props: React.HTMLAttributes<HTMLLIElement>,
      option: SelectOption<T> | SelectOption<'__SELECT_ALL__'>,
      { selected }: { selected: boolean }
    ) => {
      const isSelectAll = isSelectAllOption(option.value);

      if (multiple) {
        const checkboxSelected = isSelectAll ? isAllSelected : selected;

        return (
          <li {...props} key={String(option.value)}>
            <Checkbox
              icon={icon}
              checkedIcon={checkedIcon}
              style={{ marginRight: 8 }}
              checked={checkboxSelected}
              disabled={option.disabled}
            />
            {option.label}
          </li>
        );
      }

      return (
        <li {...props} key={String(option.value)}>
          {option.label}
        </li>
      );
    },
    [multiple, isAllSelected]
  );

  const renderTags = useCallback(
    (
      tagValue: (SelectOption<T> | SelectOption<'__SELECT_ALL__'>)[],
      getTagProps: (
        index: number
      ) => {
        key: number;
        className: string;
        onDelete: (event: React.SyntheticEvent) => void;
      }
    ) => {
      if (multiple && displayValue) {
        return [
          <Chip
            size="small"
            label={displayValue}
            {...getTagProps(0)}
            key="all"
            sx={{
              backgroundColor: theme.palette.primary.main,
              color: theme.palette.primary.contrastText,
            }}
          />,
        ];
      }

      return tagValue.map((option, index) => {
        if (isSelectAllOption(option.value)) {
          return null;
        }
        return (
          <Chip
            size="small"
            label={option.label}
            {...getTagProps(index)}
            key={String(option.value)}
          />
        );
      });
    },
    [multiple, displayValue, theme]
  );

  const renderInput = useCallback(
    (params: any) => (
      <TextField
        {...params}
        label={label}
        placeholder={placeholder}
        error={error}
        helperText={!multiple ? helperText : undefined}
        size={size}
        InputProps={{
          ...params.InputProps,
          ...(disabled && { disabled: true }),
        }}
      />
    ),
    [label, placeholder, error, helperText, size, disabled, multiple]
  );

  const autocompleteValue = useMemo(() => {
    if (multiple) {
      if (displayValue && isAllSelected) {
        return [options[0]];
      }
      return selectedOptions;
    }
    return selectedOptions.length > 0 ? selectedOptions[0] : null;
  }, [multiple, displayValue, isAllSelected, selectedOptions, options]);

  // Sync input value with selected value in single mode
  const handleInputChange = useCallback(
    (_: React.SyntheticEvent, newInputValue: string, reason: string) => {
      if (searchable || reason === 'input') {
        setInputValue(newInputValue);
      }
      // When an option is selected in single mode, update input to show label
      if (!multiple && reason === 'reset' && singleValue) {
        const selected = options.find((opt) => opt.value === singleValue);
        if (selected) {
          setInputValue(selected.label);
        }
      }
    },
    [searchable, multiple, singleValue, options]
  );

  return (
    <FormControl fullWidth error={error} disabled={disabled}>
      {multiple && helperText && <FormHelperText sx={{ mb: 1 }}>{helperText}</FormHelperText>}
      <Autocomplete
        {...restProps}
        multiple={multiple}
        options={multiple && selectAll ? optionsWithSelectAll : (options as SelectOption<T | '__SELECT_ALL__'>[])}
        value={autocompleteValue as any}
        onChange={handleChange as any}
        inputValue={searchable ? inputValue : undefined}
        onInputChange={handleInputChange}
        open={open}
        onOpen={() => setOpen(true)}
        onClose={() => setOpen(false)}
        getOptionLabel={getOptionLabel as any}
        isOptionEqualToValue={isOptionEqualToValue as any}
        renderOption={renderOption as any}
        renderTags={renderTags as any}
        renderInput={renderInput}
        disabled={disabled}
        disableCloseOnSelect={multiple}
        filterSelectedOptions
        sx={{
          [`& .${autocompleteClasses.tag}`]: {
            margin: '2px',
          },
          ...restProps.sx,
        }}
      />
    </FormControl>
  );
}
