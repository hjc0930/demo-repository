import { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  MenuItem,
  Stack,
} from "@mui/material";
import dayjs from "dayjs";
import { Todo } from "../domain/Todo";
import { Priority } from "../domain/Priority";

interface TodoFormDialogProps {
  open: boolean;
  editTodo: Todo | null; // null = 创建模式
  onClose: () => void;
  onSubmit: (payload: {
    title: string;
    description: string;
    priority: number;
    dueDate: string;
  }) => void;
}

/**
 * UI 组件：创建/编辑待办表单弹窗
 */
export function TodoFormDialog({
  open,
  editTodo,
  onClose,
  onSubmit,
}: TodoFormDialogProps) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState(Priority.MEDIUM.value);
  const [dueDate, setDueDate] = useState("");

  const isEdit = editTodo !== null;

  useEffect(() => {
    if (open) {
      if (editTodo) {
        setTitle(editTodo.title);
        setDescription(editTodo.description);
        setPriority(editTodo.priority.value);
        setDueDate(
          editTodo.dueDate
            ? dayjs(editTodo.dueDate).format("YYYY-MM-DDTHH:mm")
            : "",
        );
      } else {
        setTitle("");
        setDescription("");
        setPriority(Priority.MEDIUM.value);
        setDueDate("");
      }
    }
  }, [open, editTodo]);

  const handleSubmit = () => {
    if (!title.trim()) return;
    onSubmit({ title: title.trim(), description, priority, dueDate });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>{isEdit ? "编辑待办" : "新增待办"}</DialogTitle>

      <DialogContent>
        <Stack spacing={2} sx={{ mt: 1 }}>
          <TextField
            label="标题"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            fullWidth
            autoFocus
          />

          <TextField
            label="描述"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            multiline
            rows={3}
            fullWidth
          />

          <TextField
            label="优先级"
            value={priority}
            onChange={(e) => setPriority(Number(e.target.value))}
            select
            fullWidth
          >
            {Priority.all().map((p) => (
              <MenuItem key={p.value} value={p.value}>
                {p.label}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="截止日期"
            type="datetime-local"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
            fullWidth
            slotProps={{ inputLabel: { shrink: true } }}
          />
        </Stack>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>取消</Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={!title.trim()}
        >
          {isEdit ? "保存" : "创建"}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
