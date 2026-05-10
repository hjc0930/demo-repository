import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Chip,
  Button,
  Stack,
  Box,
} from "@mui/material";
import {
  CheckCircleOutlined,
  PlayArrowOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@mui/icons-material";
import dayjs from "dayjs";
import { Todo } from "../domain/Todo";

interface TodoCardProps {
  todo: Todo;
  onComplete: (todo: Todo) => void;
  onStartProgress: (todo: Todo) => void;
  onEdit: (todo: Todo) => void;
  onDelete: (todo: Todo) => void;
}

/**
 * UI 组件：单张待办卡片
 * 不包含业务逻辑，所有判断来自领域对象
 */
export function TodoCard({
  todo,
  onComplete,
  onStartProgress,
  onEdit,
  onDelete,
}: TodoCardProps) {
  return (
    <Card
      variant="outlined"
      sx={{
        borderLeft: `4px solid ${todo.status.color}`,
        opacity: todo.isDone() ? 0.7 : 1,
        transition: "opacity 0.2s",
      }}
    >
      <CardContent sx={{ pb: 1 }}>
        <Stack spacing={1}>
          {/* 标签行 */}
          <Stack direction="row" spacing={1}>
            <Chip
              label={todo.status.label}
              size="small"
              sx={{
                bgcolor: todo.status.color,
                color: "#fff",
              }}
            />
            <Chip label={todo.priority.label} size="small" variant="outlined" />
            {todo.isOverdue() && (
              <Chip label="已逾期" size="small" color="error" />
            )}
          </Stack>

          {/* 标题 */}
          <Typography
            variant="h6"
            sx={{
              textDecoration: todo.isDone() ? "line-through" : "none",
            }}
          >
            {todo.title}
          </Typography>

          {/* 描述 */}
          {todo.description && (
            <Typography variant="body2" color="text.secondary">
              {todo.description}
            </Typography>
          )}

          {/* 截止日期 */}
          {todo.dueDate && (
            <Box>
              <Typography
                variant="caption"
                color={todo.isOverdue() ? "error" : "text.secondary"}
              >
                截止: {dayjs(todo.dueDate).format("YYYY-MM-DD HH:mm")}
              </Typography>
            </Box>
          )}
        </Stack>
      </CardContent>

      <CardActions>
        {todo.status.canComplete() && (
          <Button
            size="small"
            startIcon={<CheckCircleOutlined />}
            onClick={() => onComplete(todo)}
          >
            完成
          </Button>
        )}
        {todo.status.canStartProgress() && (
          <Button
            size="small"
            startIcon={<PlayArrowOutlined />}
            onClick={() => onStartProgress(todo)}
          >
            开始
          </Button>
        )}
        <Button
          size="small"
          startIcon={<EditOutlined />}
          onClick={() => onEdit(todo)}
        >
          编辑
        </Button>
        <Button
          size="small"
          color="error"
          startIcon={<DeleteOutlined />}
          onClick={() => onDelete(todo)}
        >
          删除
        </Button>
      </CardActions>
    </Card>
  );
}
