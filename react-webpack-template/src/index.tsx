import { createRoot } from "react-dom/client";
import App from "./App";
// import "antd/dist/reset.css";

const root = document.getElementById("root");
if (!root) throw new Error("Root Element is not define");

const app = createRoot(root);

app.render(<App />);
