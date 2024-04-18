import React from "react";
import Table from "@/components/Table";

const App: React.FC = () => {
  const data = [
    { name: "John", age: 28, job: "Engineer" },
    { name: "Jane", age: 24, job: "Designer" },
    { name: "Doe", age: 32, job: "Teacher" },
  ];

  const columns = [
    { key: "name", title: "Name" },
    { key: "age", title: "Age", sorter: (a: any, b: any) => a.age - b.age },
    { key: "job", title: "Job" },
  ];

  return <Table data={data} columns={columns} />;
};

export default App;
