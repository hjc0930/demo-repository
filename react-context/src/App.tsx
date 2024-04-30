import { Table } from "antd";
import { isUS_Dst } from "./utils/time-zome";

function getUTCTime() {
  let d1 = new Date();
  return +new Date(
    d1.getUTCFullYear(),
    d1.getUTCMonth(),
    d1.getUTCDate(),
    d1.getUTCHours(),
    d1.getUTCMinutes(),
    d1.getUTCSeconds()
  );
}

const getESTDate = (datePara?: any) => {
  datePara = datePara ? datePara : new Date();
  return Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "numeric",
    day: "numeric",
    hour: "numeric",
    minute: "numeric",
    timeZone: "America/New_York",
    timeZoneName: "shortOffset",
  }).format(new Date());
};

function App() {
  const getDate = getESTDate();

  console.log({ getDate });

  return <Table />;
}

export default App;

// const utcDate = new Date().toUTCString();
// const estDate = new Date().toLocaleString("en-US", {
//   timeZone: "America/New_York",
// });

// const options: any = {
//   year: "numeric",
//   month: "numeric",
//   day: "numeric",
//   hour: "numeric",
//   minute: "numeric",
//   second: "numeric",
//   timeZoneName: "longGeneric",
// };

// const date = Intl.DateTimeFormat("en-US", {
//   ...options,
// }).format(new Date());
// const estDateIntl = new Intl.DateTimeFormat("en-US", {
//   ...options,
//   timeZone: "America/New_York",
// }).format(new Date("4/30/2024, 5:52:51 AM"));

// console.log({ date, estDateIntl });
