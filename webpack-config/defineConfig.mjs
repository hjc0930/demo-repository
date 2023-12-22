const defineConfig = (callback) => {
  const result = callback("dev", {});
  return result;
};

export default defineConfig;
