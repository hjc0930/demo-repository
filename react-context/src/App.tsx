import { Button } from "antd";
import useConfirm from "./hooks/useConfirm";

function App() {
  const [confirm, context] = useConfirm();

  const handleClick = () => {
    confirm({
      title: "Title",
      content: "Content",
      onOk: () => {
        console.log("ok");
      },
      onCancel: () => {
        console.log("cancel");
      },
    });
  };

  return (
    <>
      <Button onClick={handleClick}>Add</Button>
      {context}
    </>
  );
}

export default App;
