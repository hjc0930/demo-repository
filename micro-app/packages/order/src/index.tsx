import { createRoot } from "react-dom/client";
import App from "./App";

const root = document.getElementById("root");
if (!root) throw new Error("Root Element is not define");

const app = createRoot(root);

app.render(<App />);
