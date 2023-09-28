import { createContext, useState } from "react";
import "./App.css";
import Child from "./views/Child";
import Home from "./views/Home";

export const ThemeContext = createContext("light");
export const CurrentUserContext = createContext<any>(null);

function App() {
  const [theme, setTheme] = useState("light");
  const [currentUser, setCurrentUser] = useState(null);
  return (
    <>
      <ThemeContext.Provider value={theme}>
        <CurrentUserContext.Provider
          value={{
            currentUser,
            setCurrentUser,
          }}
        >
          <Home />
          <Child />
          <button
            onClick={() => setTheme(theme === "light" ? "dark" : "light")}
          >
            Toggle Theme
          </button>
        </CurrentUserContext.Provider>
      </ThemeContext.Provider>
    </>
  );
}

export default App;

// 我觉得终身学习很重要

// I think lifelong learning is very important
