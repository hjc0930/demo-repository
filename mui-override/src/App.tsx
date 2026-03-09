import { useState } from "react";
import { Box, Container, Typography, Stack, Paper } from "@mui/material";
import { CustomSelect, SelectOption } from "./components/CustomSelect";

interface User {
  id: number;
  name: string;
}

const singleOptions: SelectOption<string>[] = [
  { value: "apple", label: "Apple" },
  { value: "banana", label: "Banana" },
  { value: "cherry", label: "Cherry" },
  { value: "date", label: "Date" },
  { value: "elderberry", label: "Elderberry" },
];

const multiOptions: SelectOption<number>[] = [
  { value: 1, label: "Option 1" },
  { value: 2, label: "Option 2" },
  { value: 3, label: "Option 3" },
  { value: 4, label: "Option 4" },
  { value: 5, label: "Option 5" },
];

const userOptions: SelectOption<User>[] = [
  { value: { id: 1, name: "John" }, label: "John" },
  { value: { id: 2, name: "Jane" }, label: "Jane" },
  { value: { id: 3, name: "Bob" }, label: "Bob" },
  { value: { id: 4, name: "Alice" }, label: "Alice" },
];

const autoCompleteOptions: SelectOption<string>[] = [
  { value: "react", label: "React" },
  { value: "react-native", label: "React Native" },
  { value: "vue", label: "Vue" },
  { value: "vue-router", label: "Vue Router" },
  { value: "angular", label: "Angular" },
];

function App() {
  const [singleValue, setSingleValue] = useState<string | null>(null);
  const [multiValue, setMultiValue] = useState<number[]>([]);
  const [userValue, setUserValue] = useState<User | null>(null);
  const [autoCompleteValue, setAutoCompleteValue] = useState<string | null>(null);

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" gutterBottom>
        CustomSelect Examples
      </Typography>

      <Stack spacing={4}>
        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Single Select
          </Typography>
          <CustomSelect
            options={singleOptions}
            value={singleValue}
            onChange={(_, value) => setSingleValue(value)}
            label="Select a fruit"
            placeholder="Choose one..."
            searchable
          />
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Selected: {singleValue ?? "none"}
          </Typography>
        </Paper>

        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Multi Select with Select All
          </Typography>
          <CustomSelect
            options={multiOptions}
            value={multiValue}
            onChange={(_, value) => setMultiValue(value as number[])}
            multiple
            selectAll
            searchable
            label="Select options"
            placeholder="Choose multiple..."
          />
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Selected: {multiValue.length > 0 ? multiValue.join(", ") : "none"}
          </Typography>
        </Paper>

        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Single Select with Object Values
          </Typography>
          <CustomSelect
            options={userOptions}
            value={userValue}
            onChange={(_, value) => setUserValue(value as User)}
            label="Select a user"
            placeholder="Choose a user..."
            searchable
          />
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Selected: {userValue ? `${userValue.name} (ID: ${userValue.id})` : "none"}
          </Typography>
        </Paper>

        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Auto Complete
          </Typography>
          <CustomSelect
            options={autoCompleteOptions}
            value={autoCompleteValue}
            onChange={(_, value) => setAutoCompleteValue(value as string)}
            label="Framework"
            placeholder="Type to filter..."
            searchable
            autoCompleted
          />
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Selected: {autoCompleteValue ?? "none"}
            <br />
            <em>Try typing to filter down to a single option - it will auto-select when closed</em>
          </Typography>
        </Paper>

        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Error State
          </Typography>
          <CustomSelect
            options={singleOptions}
            value={null}
            label="Required field"
            placeholder="Select something..."
            error
            helperText="This field is required"
          />
        </Paper>

        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Disabled State
          </Typography>
          <CustomSelect
            options={singleOptions}
            value="apple"
            label="Disabled select"
            disabled
          />
        </Paper>
      </Stack>
    </Container>
  );
}

export default App;
