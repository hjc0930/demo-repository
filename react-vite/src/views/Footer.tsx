import { useEffect } from "react";
import useStores from "./useStore";

function Footer() {
  const { state } = useStores();
  const { list } = state;

  useEffect(() => {
    console.log("Footer page rendered!!!");
  }, [list]);
  return (
    <div style={{ height: 200 }}>
      <div>
        list is
        {list.map((item) => (
          <span
            style={{
              background: "pink",
              padding: "0 10px",
              border: "1px solid",
              marginRight: "10px",
            }}
            key={item.key}
          >
            {item.num}
          </span>
        ))}
      </div>
    </div>
  );
}

export default Footer;
