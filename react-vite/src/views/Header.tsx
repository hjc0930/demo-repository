import useStores from "./useStore";

function Header() {
  // 解构获取 dispatch 方法
  const { dispatch } = useStores();
  const handleList = () => {
    const payload = [...new Array(3)].map(() => {
      const key = Math.random();
      const num = Math.floor(key * 100);
      return {
        key,
        num,
      };
    });
    // 更新数据，订阅状态的组件都会获取更新通知并取到最新数据
    dispatch({ type: "list", payload });
  };
  return (
    <div style={{ height: 100 }}>
      <button onClick={() => dispatch({ type: "time", payload: Date.now() })}>
        time
      </button>
      <button onClick={handleList}>list</button>
    </div>
  );
}

export default Header;
