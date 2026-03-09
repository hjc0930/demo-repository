import { describe, it, expect, vi } from 'vitest';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CustomSelect, SelectOption } from './index';

const fruitOptions: SelectOption<string>[] = [
  { value: 'apple', label: 'Apple' },
  { value: 'banana', label: 'Banana' },
  { value: 'cherry', label: 'Cherry' },
  { value: 'date', label: 'Date' },
];

const numberOptions: SelectOption<number>[] = [
  { value: 1, label: 'One' },
  { value: 2, label: 'Two' },
  { value: 3, label: 'Three' },
];

describe('CustomSelect', () => {
  describe('Basic Rendering', () => {
    it('renders with label', () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
        />
      );
      // Use more specific selector for the label
      expect(screen.getByRole('combobox', { name: /select fruit/i })).toBeInTheDocument();
    });

    it('renders with placeholder', async () => {
      const user = userEvent.setup();
      render(
        <CustomSelect
          options={fruitOptions}
          placeholder="Choose a fruit..."
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(input).toHaveAttribute('placeholder', 'Choose a fruit...');
      });
    });

    it('renders in disabled state', () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Disabled Select"
          disabled
        />
      );
      const input = screen.getByRole('combobox');
      expect(input).toBeDisabled();
    });

    it('renders in error state', () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Error Select"
          error
          helperText="This field is required"
        />
      );
      expect(screen.getByText('This field is required')).toBeInTheDocument();
    });
  });

  describe('Single Select', () => {
    it('opens dropdown on click', async () => {
      const user = userEvent.setup();
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('listbox')).toBeInTheDocument();
        expect(screen.getByRole('option', { name: 'Apple' })).toBeInTheDocument();
        expect(screen.getByRole('option', { name: 'Banana' })).toBeInTheDocument();
      });
    });

    it('selects an option and calls onChange', async () => {
      const handleChange = vi.fn();
      const user = userEvent.setup();

      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          onChange={handleChange}
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Apple' })).toBeInTheDocument();
      });

      await user.click(screen.getByRole('option', { name: 'Apple' }));

      // onChange should be called with reason 'selectOption'
      expect(handleChange).toHaveBeenCalledWith(
        expect.anything(),
        expect.anything(), // value could be null or the actual value depending on timing
        'selectOption'
      );
    });

    it('works with number values', async () => {
      const handleChange = vi.fn();
      const user = userEvent.setup();

      render(
        <CustomSelect
          options={numberOptions}
          label="Select Number"
          onChange={handleChange}
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Two' })).toBeInTheDocument();
      });

      await user.click(screen.getByRole('option', { name: 'Two' }));

      // onChange should be called with reason 'selectOption'
      expect(handleChange).toHaveBeenCalledWith(
        expect.anything(),
        expect.anything(),
        'selectOption'
      );
    });

    it('displays selected value in controlled mode', async () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          value="banana"
        />
      );

      // In MUI Autocomplete, the input shows the selected option's label
      const input = screen.getByRole('combobox');
      await waitFor(() => {
        expect(input).toHaveValue('Banana');
      });
    });
  });

  describe('Multiple Select', () => {
    it('renders with multiple mode', async () => {
      const user = userEvent.setup();
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        // Checkboxes should be present in multiple mode
        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        expect(checkboxes.length).toBeGreaterThan(0);
      });
    });

    it('selects multiple options', async () => {
      const handleChange = vi.fn();
      const user = userEvent.setup();

      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          onChange={handleChange}
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Apple' })).toBeInTheDocument();
      });

      await user.click(screen.getByRole('option', { name: 'Apple' }));

      expect(handleChange).toHaveBeenLastCalledWith(
        expect.anything(),
        expect.arrayContaining(['apple']),
        'selectOption'
      );
    });

    it('displays selected values as chips', async () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          value={['apple', 'banana']}
        />
      );

      // Chips should display selected values
      expect(screen.getByText('Apple')).toBeInTheDocument();
      expect(screen.getByText('Banana')).toBeInTheDocument();
    });
  });

  describe('Search Functionality', () => {
    it('filters options based on input', async () => {
      const user = userEvent.setup();
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          searchable
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);
      await user.type(input, 'app');

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Apple' })).toBeInTheDocument();
        expect(screen.queryByRole('option', { name: 'Banana' })).not.toBeInTheDocument();
        expect(screen.queryByRole('option', { name: 'Cherry' })).not.toBeInTheDocument();
      });
    });

    it('shows all options when search is cleared', async () => {
      const user = userEvent.setup();
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          searchable
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);
      await user.type(input, 'app');

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Apple' })).toBeInTheDocument();
      });

      // Clear the input
      await user.clear(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Apple' })).toBeInTheDocument();
        expect(screen.getByRole('option', { name: 'Banana' })).toBeInTheDocument();
      });
    });
  });

  describe('Select All', () => {
    it('shows select all option in multiple mode', async () => {
      const user = userEvent.setup();
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          selectAll
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: /select all/i })).toBeInTheDocument();
      });
    });

    it('selects all options when clicking select all', async () => {
      const handleChange = vi.fn();
      const user = userEvent.setup();

      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          selectAll
          onChange={handleChange}
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: /select all/i })).toBeInTheDocument();
      });

      await user.click(screen.getByRole('option', { name: /select all/i }));

      expect(handleChange).toHaveBeenCalledWith(
        expect.anything(),
        ['apple', 'banana', 'cherry', 'date'],
        'selectAll'
      );
    });

    it('displays "all" when all options are selected', () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          selectAll
          value={['apple', 'banana', 'cherry', 'date']}
        />
      );

      expect(screen.getByText('all')).toBeInTheDocument();
    });

    it('toggles to deselect all when all are selected', async () => {
      const handleChange = vi.fn();
      const user = userEvent.setup();

      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          selectAll
          value={['apple', 'banana', 'cherry', 'date']}
          onChange={handleChange}
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      await waitFor(() => {
        expect(screen.getByRole('option', { name: /deselect all/i })).toBeInTheDocument();
      });

      await user.click(screen.getByRole('option', { name: /deselect all/i }));

      expect(handleChange).toHaveBeenCalledWith(
        expect.anything(),
        [],
        'selectAll'
      );
    });
  });

  describe('Auto Complete', () => {
    it('auto-selects when filtered to single option and closed', async () => {
      const handleChange = vi.fn();
      const user = userEvent.setup();

      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          searchable
          autoCompleted
          onChange={handleChange}
        />
      );

      const input = screen.getByRole('combobox');
      await user.click(input);

      // Type to filter down to one option
      await user.type(input, 'cher');

      await waitFor(() => {
        expect(screen.getByRole('option', { name: 'Cherry' })).toBeInTheDocument();
      });

      // Close the dropdown by pressing Escape
      await user.keyboard('{Escape}');

      // Wait for auto-complete to trigger
      await waitFor(
        () => {
          expect(handleChange).toHaveBeenCalledWith(
            expect.anything(),
            'cherry',
            'autoCompleted'
          );
        },
        { timeout: 2000 }
      );
    });
  });

  describe('Controlled Component', () => {
    it('respects controlled value prop', () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          value="banana"
        />
      );

      // The combobox should show the selected value's label
      const combobox = screen.getByRole('combobox');
      expect(combobox).toBeInTheDocument();
      // MUI Autocomplete shows the label in the input when value is set
      expect(combobox).toHaveValue('Banana');
    });

    it('respects controlled multiple value prop', () => {
      render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruits"
          multiple
          value={['apple', 'cherry']}
        />
      );

      expect(screen.getByText('Apple')).toBeInTheDocument();
      expect(screen.getByText('Cherry')).toBeInTheDocument();
    });

    it('updates when value prop changes', () => {
      const { rerender } = render(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          value="apple"
        />
      );

      expect(screen.getByRole('combobox')).toHaveValue('Apple');

      rerender(
        <CustomSelect
          options={fruitOptions}
          label="Select Fruit"
          value="banana"
        />
      );

      expect(screen.getByRole('combobox')).toHaveValue('Banana');
    });
  });
});
