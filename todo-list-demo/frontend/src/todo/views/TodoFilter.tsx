import { Stack, Chip } from "@mui/material";
import { TodoStatus } from "../domain/TodoStatus";

interface TodoFilterProps {
  current: TodoStatus | null;
  onChange: (status: TodoStatus | null) => void;
}

/**
 * UI 组件：状态筛选器
 * 使用领域值对象生成筛选项
 */
export function TodoFilter({ current, onChange }: TodoFilterProps) {
  return (
    <Stack direction="row" spacing={1}>
      <Chip
        label="全部"
        variant={current === null ? "filled" : "outlined"}
        color={current === null ? "primary" : "default"}
        onClick={() => onChange(null)}
      />
      {TodoStatus.all().map((s) => (
        <Chip
          key={s.value}
          label={s.label}
          variant={current?.value === s.value ? "filled" : "outlined"}
          color={current?.value === s.value ? "primary" : "default"}
          onClick={() => onChange(s)}
        />
      ))}
    </Stack>
  );
}
