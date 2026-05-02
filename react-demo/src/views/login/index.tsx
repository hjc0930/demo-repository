import {
  Box,
  Button,
  Card,
  FormControl,
  FormLabel,
  Stack,
  TextField,
  Typography,
} from "@mui/material";

import { useMutation } from "@tanstack/react-query";
import { useForm } from "@tanstack/react-form";

import { login } from "@/services/auth";
import type { LoginRequest } from "@/types/auth";

const LoginPage = () => {
  const loginMutation = useMutation({
    mutationFn: (data: LoginRequest) => login(data),
  });

  const form = useForm({
    defaultValues: {
      username: "",
      password: "",
    },
    onSubmit: ({ value }) => loginMutation.mutate(value),
  });

  return (
    <Card
      sx={{
        maxWidth: 520,
        margin: "auto",
        mt: 16,
        p: 2,
        boxShadow: "0 4px 24px rgba(0, 0, 0, 0.1)",
      }}
    >
      <Typography variant="h4" gutterBottom>
        Login
      </Typography>
      <Box
        component="form"
        onSubmit={(e) => {
          e.preventDefault();
          e.stopPropagation();
          form.handleSubmit();
        }}
      >
        <Stack direction="column" spacing={3}>
          <form.Field
            name="username"
            validators={{
              onChange: ({ value }) =>
                !value ? "Username is required" : undefined,
            }}
          >
            {(field) => {
              return (
                <FormControl fullWidth>
                  <FormLabel htmlFor={field.name}>Username</FormLabel>
                  <TextField
                    error={!!field.state.meta.errorMap.onChange}
                    helperText={field.state.meta.errorMap.onChange}
                    id={field.name}
                    variant="standard"
                    placeholder="Enter your username"
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(e) => field.handleChange(e.target.value)}
                  />
                </FormControl>
              );
            }}
          </form.Field>
          <form.Field
            name="password"
            validators={{
              onChange: ({ value }) =>
                !value ? "Password is required" : undefined,
            }}
          >
            {(field) => (
              <FormControl fullWidth>
                <FormLabel htmlFor={field.name}>Password</FormLabel>
                <TextField
                  error={!!field.state.meta.errorMap.onChange}
                  helperText={field.state.meta.errorMap.onChange}
                  id={field.name}
                  variant="standard"
                  itemType="password"
                  placeholder="Enter your password"
                  type="password"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(e) => field.handleChange(e.target.value)}
                />
              </FormControl>
            )}
          </form.Field>
          <form.Subscribe
            selector={(state) => [state.canSubmit, state.values] as const}
          >
            {([canSubmit, values]) => {
              const canSubmitForm =
                canSubmit &&
                Object.values(values).every((value) => value !== "");

              return (
                <FormControl fullWidth>
                  <Button
                    variant="contained"
                    disabled={!canSubmitForm || loginMutation.isPending}
                    loading={loginMutation.isPending}
                    type="submit"
                  >
                    Login
                  </Button>
                </FormControl>
              );
            }}
          </form.Subscribe>
        </Stack>
      </Box>
    </Card>
  );
};

export default LoginPage;
