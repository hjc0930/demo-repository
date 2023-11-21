import { useEffect, useState } from "react";
import { Space, Table, Tag } from "antd";

const columns = [
  {
    title: "Key",
    dataIndex: "key",
    key: "key",
  },
  {
    title: "Name",
    dataIndex: "name",
    key: "name",
    render: (text: string) => <a>{text}</a>,
  },
  {
    title: "Age",
    dataIndex: "age",
    key: "age",
  },
  {
    title: "Address",
    dataIndex: "address",
    key: "address",
  },
  {
    title: "Tags",
    key: "tags",
    dataIndex: "tags",
    render: (_: any, { tags }: any) => (
      <>
        {tags.map((tag: any) => {
          let color = tag.length > 5 ? "geekblue" : "green";
          if (tag === "loser") {
            color = "volcano";
          }
          return (
            <Tag color={color} key={tag}>
              {tag.toUpperCase()}
            </Tag>
          );
        })}
      </>
    ),
  },
  {
    title: "Action",
    key: "action",
    render: (_: any, record: any) => (
      <Space size="middle">
        <a>Invite {record.name}</a>
        <a>Delete</a>
      </Space>
    ),
  },
];

const data = new Array(100000).fill("*").map((_, key) => ({
  key: key + 1,
  name: "John Brown",
  age: 32,
  address: "New York No. 1 Lake Park",
  tags: ["nice", "developer"],
}));

function App() {
  const [dataSource, setDataSource] = useState<any[]>([]);

  useEffect(() => {
    setDataSource(data);
  }, []);
  return (
    <Table
      rowKey="key"
      virtual
      scroll={{
        y: 600,
      }}
      pagination={false}
      columns={columns}
      dataSource={dataSource}
    />
  );
}

export default App;
