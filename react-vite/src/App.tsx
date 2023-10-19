import { Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useState } from "react";
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

  // const _runTask = (task: any) => {
  //   requestIdleCallback((idle) => {
  //     if (idle.timeRemaining() > 0) {
  //       task();
  //     } else {
  //       _runTask(task);
  //     }
  //   });
  // };

  const addData = async () => {
    const total = 5000;
    let i = 0;

    const _run = () => {
      const startTime = Date.now();
      while (i < total && Date.now() - startTime < 16.6) {
        i++;
        setData((val) => [
          ...val,
          {
            id: i + 1,
            name: `name-${i + 1}`,
          },
        ]);
      }

      if (i < total) {
        requestAnimationFrame(_run);
      }
    };

    _run();
    // for (let i = 0; i < 100; i++) {
    //   _runTask(() => {
    //     setData((val) => [
    //       ...val,
    //       {
    //         id: i + 1,
    //         name: `name-${i + 1}`,
    //       },
    //     ]);
    //   });
    // }
  };

  console.log(data);

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
