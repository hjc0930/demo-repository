import "./App.css";

const getTintVariantColor = (color: string) => {
  return {
    50: `color-mix(in srgb, ${color} 5%, white)`,
    100: `color-mix(in srgb, ${color} 10%, white)`,
    200: `color-mix(in srgb, ${color} 30%, white)`,
    300: `color-mix(in srgb, ${color} 50%, white)`,
    400: `color-mix(in srgb, ${color} 70%, white)`,
    500: `${color}`,
    600: `color-mix(in srgb, ${color}, black 10%)`,
    700: `color-mix(in srgb, ${color}, black 30%)`,
    800: `color-mix(in srgb, ${color}, black 50%)`,
    900: `color-mix(in srgb, ${color}, black 70%)`,
  };
};

function App() {
  const result = getTintVariantColor("#96454c");

  return (
    <ul>
      {Object.entries(result).map(([key, value], index) => (
        <li
          style={{
            backgroundColor: value,
            color: index < 5 ? "#000000" : "#ffffff",
          }}
        >
          {key}
        </li>
      ))}
    </ul>
  );
}

export default App;
