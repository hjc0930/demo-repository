import RenderBody from "./RenderBody";
import styles from "./index.module.css";
import { Fragment, useState } from "react";
import { Button, Dropdown, Flex, Input, Tooltip } from "antd";
import { EllipsisOutlined, SearchOutlined } from "@ant-design/icons";

const TreeTable = () => {
  const [data] = useState([
    {
      id: 1,
      name: "John",
      age: 30,
      aClass: "A",
      children: [
        {
          id: 2,
          name: "Jane",
          age: 25,
          aClass: "A",
          children: [
            {
              id: 3,
              name: "Jack-1",
              age: 10,
              aClass: "A",
            },
            {
              id: 4,
              name: "Jack-2",
              age: 10,
              aClass: "A",
            },
            {
              id: 5,
              name: "Jack-3",
              age: 10,
              aClass: "A",
            },
            {
              id: 6,
              name: "Jack-4",
              age: 10,
              aClass: "A",
            },
          ],
        },
      ],
    },
    {
      id: 7,
      name: "John",
      age: 30,
      aClass: "A",
    },
  ]);

  const renderTree = (data: any, expandKey?: string, treeKey = 0) => {
    return data.map((item: any) => {
      const isExpand = expandKey ? item[expandKey] : false;
      return (
        <Fragment key={item.name}>
          <tr data-tree-key={treeKey}>
            <td
              style={{
                paddingLeft: treeKey * 20 + "px",
              }}
            >
              {item.name}
            </td>
            <td>{item.age}</td>
            <td>{item.aClass}</td>
          </tr>
          {item.children &&
            isExpand &&
            renderTree(item.children, expandKey, treeKey + 1)}
        </Fragment>
      );
    });
  };

  const onSearch = (value: string) => {
    console.log(value);
  };

  function traverseTree(nodes: any[]) {
    let stack = [];
    let currentNode = nodes;

    while (currentNode || stack.length > 0) {
      while (currentNode) {
        console.log(currentNode.name); // 处理当前节点
        stack.push(currentNode);
        currentNode = currentNode.children;
      }

      currentNode = stack.pop();
      if (currentNode.children) {
        currentNode = currentNode.children;
      }
    }
  }

  traverseTree(data);

  // TODO: 实现树形表格组件
  return (
    <table className={styles.table}>
      <colgroup>
        <col width={300} />
      </colgroup>
      <thead>
        <tr>
          <th
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            Name
            <Dropdown
              placement="bottomRight"
              trigger={["click"]}
              dropdownRender={() => (
                <Flex style={{ padding: "10px", backgroundColor: "#fff" }}>
                  <Input.Search onSearch={onSearch} />
                </Flex>
              )}
            >
              <EllipsisOutlined style={{ transform: "rotate(90deg)" }} />
            </Dropdown>
          </th>
          <th>Age</th>
          <th>aClass</th>
        </tr>
      </thead>
      <RenderBody data={data} />
    </table>
  );
};

export default TreeTable;
