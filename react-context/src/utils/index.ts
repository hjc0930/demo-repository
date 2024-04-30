type _Date = string | number | Date;

export const getESTDate = (datePara?: DatePara) => {
  datePara = datePara ? datePara : new Date();
  DEFAULT_OPTIONS.timeZone = "America/New_York";
  return new Date(datePara).toLocaleString("en-US", DEFAULT_OPTIONS);
};

/** @以下是获取UTC0时区的数据 */
export const UTC_getYear = (datePara?: _Date) =>
  new Date(datePara as _Date).getUTCFullYear();
export const UTC_getMonth = (datePara?: _Date) =>
  new Date(datePara as _Date).getUTCMonth();
export const UTC_getDate = (datePara?: _Date) =>
  new Date(datePara as _Date).getUTCDate();
export const UTC_getHours = (datePara?: _Date) =>
  new Date(datePara as _Date).getUTCHours();
export const UTC_getMinutes = (datePara?: _Date) =>
  new Date(datePara as _Date).getUTCMinutes();
export const UTC_getSeconds = (datePara?: _Date) =>
  new Date(datePara as _Date).getUTCSeconds();

/** @以下是EST时间 */
export const getESTmmddyyyy = (datePara?: _Date) => {
  return getESTDate(datePara).split(" ")[1].replace(",", ""); // 月 日 年
};
export const getESThms = (datePara?: _Date) => {
  return getESTDate(datePara).split(" ")[2]; // 时 分 秒
};
export const EST_getYear = (datePara?: _Date) => {
  return Number(getESTmmddyyyy(datePara).split("/")[2]);
};
export const EST_getMonth = (datePara?: _Date) => {
  return Number(getESTmmddyyyy(datePara).split("/")[0]) - 1;
};
export const EST_getDate = (datePara?: _Date) => {
  return Number(getESTmmddyyyy(datePara).split("/")[1]);
};
export const EST_getHours = (is0?: number, datePara?: _Date) => {
  let data = Number(getESThms(datePara).split(":")[0]);
  if (is0 === 0 && Number(data) === 24) {
    data = 0;
  }
  return Number(data);
};
export const EST_getMinutes = (datePara?: _Date) => {
  return Number(getESThms(datePara).split(":")[1]);
};
export const EST_getSeconds = (datePara?: _Date) => {
  return Number(getESThms(datePara).split(":")[2]);
};
export const EST_getDay = (datePara?: _Date) => {
  let usDay = getESTDate(datePara).split(" ")[0].replace(",", "");
  let DAY = week.findIndex((e: string) => {
    return e == usDay;
  });
  return DAY;
};
/** @以上是EST时间 */
export const isUS_Dst = () => {
  // 美国现在是否夏令时 GMT-4
  let UTCyear = UTC_getYear();
  let UTCmonth = UTC_getMonth();
  let UTCdate = UTC_getDate();
  let UTChours = UTC_getHours();
  let UTCminutes = UTC_getMinutes();
  let UTCseconds = UTC_getSeconds();
  let us = +new Date(
    EST_getYear(),
    EST_getMonth(),
    EST_getDate(),
    EST_getHours(0),
    EST_getMinutes(),
    EST_getSeconds()
  );
  let utc = +new Date(
    UTCyear,
    UTCmonth,
    UTCdate,
    UTChours,
    UTCminutes,
    UTCseconds
  );
  let timeDiff = (us - utc) / 1000 / 60 / 60;
  if (timeDiff == -5) {
    return false;
  } else {
    return true;
  }
};
