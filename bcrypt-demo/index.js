// app.js
import express, { json } from "express";
const app = express();

app.use(json());

// 示例路由
app.get("/api/users", (req, res) => {
  res.json([
    { id: 1, name: "John" },
    { id: 2, name: "Jane" },
  ]);
});

app.get("/api/users/:id", (req, res) => {
  const user = { id: parseInt(req.params.id), name: "Test User" };
  res.json(user);
});

app.post("/api/users", (req, res) => {
  const newUser = { id: Date.now(), ...req.body };
  res.status(201).json(newUser);
});

export default app;
