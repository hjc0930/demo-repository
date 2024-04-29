import { Button } from "antd";
import { useState } from "react";

const ExpandableBtn = (props: { expanded: boolean; onChange: () => void }) => {
  const { expanded, onChange } = props;

  return <Button onClick={onChange}>{expanded.toString()}</Button>;
};

const useExpandable = (isOpen: boolean) => {
  const [expandes, setExpandes] = useState<number[]>([]);

  const onChange = (key: number) => {
    const newExpandes = expandes.some((item) => item === key)
      ? expandes.filter((item) => item !== key)
      : [...expandes, key].toSorted();

    setExpandes([...newExpandes]);
  };

  let columns;

  if (isOpen) {
    columns = {
      title: "Opts",
      dataIndex: "opts",
      render: (value, record, index: number) => {
        console.log(expandes);

        return (
          <ExpandableBtn
            expanded={expandes.includes(index)}
            onChange={() => onChange(index)}
          />
        );
      },
    };
  }
  return [columns];
};

export default useExpandable;
