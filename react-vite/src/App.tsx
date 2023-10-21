import { Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import { startTransition, useState } from "react";
// import

const App = () => {
  const [data, setData] = useState<any[]>([]);
  const [text, setText] = useState("");

  const columns: ColumnsType = [
    {
      title: "ID",
      dataIndex: "id",
    },
    {
      title: "Name",
      dataIndex: "name",
    },
  ];

  const addData = async () => {
    const total = 5000;
    const pageNum = 100;
    const pageTotal = Math.ceil(total / pageNum);
    let page = 1;

    const _run = () => {
      for (let i = (page - 1) * pageNum; i < page * pageNum; i++) {
        data.push({
          id: i + 1,
          name: `name-${i + 1}`,
        });
      }
      startTransition(() => {
        setData([...data]);
      });
      page++;

      if (page <= pageTotal) {
        requestAnimationFrame(_run);
      }
    };

    requestAnimationFrame(_run);
  };

  return (
    <div>
      <input
        type="text"
        value={text}
        onChange={(e) => setText(e.target.value)}
      />
      <p>{text}</p>
      <button id="my-button" onClick={addData}>
        执行任务
      </button>
      <Table
        columns={columns as any}
        dataSource={data}
        rowKey="id"
        scroll={{
          y: 500,
        }}
        pagination={false}
      />
      {/* <div
        id="box"
        style={{
          width: 400,
          height: 500,
          overflow: "auto",
          marginTop: "24px",
        }}
      >
        {new Array(1000).fill("*").map((_, index) => (
          <h2 key={index}>{index}</h2>
        ))}
      </div> */}
    </div>
  );
};

export default App;
