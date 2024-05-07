import { Pagination, Table } from "antd";

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
  console.log(
    Intl.DateTimeFormat("en-us", {
      timeZone: "America/New_York",
      timeZoneName: "longOffset",
    }).format(new Date())
  );

  return (
    <Pagination
      pageSize={50}
      total={40}
      size="small"
      showTotal={(total, range) => {
        console.log(total, range);

        return `${range[0]}-${range[1]} of ${total} items`;
      }}
    />
  );
}

export default App;
