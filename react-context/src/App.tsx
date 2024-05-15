import { Input, Pagination, Radio, Table, Breadcrumb } from "antd";

const getESTDate = (datePara?: any) => {
  datePara = datePara ? datePara : new Date();
  return Intl.DateTimeFormat("en-US", {
    // year: "numeric",
    // month: "numeric",
    // day: "numeric",
    // hour: "numeric",
    // minute: "numeric",
    timeZone: "America/New_York",
    timeZoneName: "shortOffset",
  }).format(new Date());
};

function App() {
  // console.log(
  //   Intl.DateTimeFormat("en-us", {
  //     timeZone: "America/New_York",
  //     timeZoneName: "longOffset",
  //   }).format(new Date())
  // );

  return (
    <>
      <Breadcrumb
        params={{ a: 1, b: 2 }}
        itemRender={(currentRoute, params, items, paths) => {
          console.log({ currentRoute, params, items, paths });

          return null;
        }}
        items={[
          {
            title: "A",
            key: 1,

            path: "/a",
          },
          {
            title: "B",
            key: 2,
            path: "/b",
          },
          {
            title: "C",
            key: 3,
            path: "c",
          },
        ]}
      />
      {/* <Pagination
        pageSize={50}
        total={40}
        size="small"
        showTotal={(total, range) => {
          console.log(total, range);

          return `${range[0]}-${range[1]} of ${total} items`;
        }}
      /> */}
      <Input readOnly />
    </>
  );
}

export default App;
