import React, { useState } from "react";

interface RenderBodyProps {
  expandKey?: string;
  onExpandChange?: (record: any, index: number) => void;
  data?: any[];
}

const RenderBody = (props: RenderBodyProps) => {
  const { data } = props;
  const [expanded, setExpanded] = useState<number[]>([]);

  const onExpandChange = (record: any, index: number) => {
    setExpanded((val) =>
      val.includes(record.id)
        ? val.filter((i) => i !== record.id)
        : [...val, record.id]
    );
  };

  const renderTree = (data: any, treeKey = 0) => {
    return data.map((item: any, index: number) => {
      const isExpanded = expanded.includes(item.id);
      return (
        <React.Fragment key={index}>
          <tr data-tree-key={treeKey}>
            <td
              colSpan={item.id === 1 ? 3 : 1}
              style={{
                paddingLeft: treeKey * 20 + "px",
              }}
            >
              {item.children ? (
                <button onClick={() => onExpandChange?.(item, treeKey)}>
                  {isExpanded ? "-" : "+"}
                </button>
              ) : null}
              {item.name}
            </td>
            {item.id !== 1 ? (
              <>
                <td>{item.age}</td>
                <td>{item.aClass}</td>
              </>
            ) : null}
          </tr>
          {item.children &&
            isExpanded &&
            renderTree(item.children, treeKey + 1)}
        </React.Fragment>
      );
    });
  };
  return <tbody>{renderTree(data)}</tbody>;
};

export default RenderBody;
