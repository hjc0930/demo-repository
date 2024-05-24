export interface FormatJsonOption<T> {
  /** 表头字段 */
  header: string;
  /** 对应表头的字段`key` */
  key: keyof T;
  /**
   * 条件处理函数
   * @param item 表单当前项
   * @param index 索引
   */
  handle?: (item: T, index: number) => number | string;
}

/**
 * 格式化`json`返回导出表格需要的数据
 * @param target 处理的目标数组
 * @param options 处理配置数组，字段顺序按照这个来
 */
export function formatJson<T>(
  target: Array<T>,
  options: Array<FormatJsonOption<T>>
) {
  const headers = options.map((item) => item.header);
  const list: Array<Array<string | number>> = [];

  for (let i = 0; i < target.length; i++) {
    const item = target[i];
    list[i] = [];
    for (let j = 0; j < options.length; j++) {
      const option = options[j];
      const key = option.key;
      if (Object.prototype.hasOwnProperty.call(item, key)) {
        if (option.handle) {
          list[i].push(option.handle(item, i));
        } else {
          list[i].push(item[key] as any);
        }
      } else {
        console.warn("function formatJson >> item 中不存在对应的 key 值");
      }
    }
  }
  return {
    headers,
    list,
  };
}

interface NativeExportOptions {
  /** 表格头部列表 */
  header: Array<string>;
  /**
   * 添加的表格头部
   * - 在固定头部的前面插入
   */
  insertHeader?: string;
  /** 导出的表格数据（二维数组） */
  data: Array<Array<string | number>>;
  /** 导出的文件名 */
  fileName: string;
  /** 表格文字排版 */
  textAlign?: "left" | "center" | "right";
  /** 图片尺寸配置 */
  imgSize?: {
    /** 图片宽度，默认 100 */
    width?: number;
    /** 图片宽度，默认 100 */
    height?: number;
  };
}

/**
 * 原生导出`Excel`函数
 * @param option
 */
export function exportExcelByNative(option: NativeExportOptions) {
  /** 字符串中包含`http`则默认为图片地址 */
  const reg = /http/;
  /** 表头的长度 */
  const headLength = option.header.length;
  /** 记录条数 */
  const tableLength = option.data.length;
  /** 设置图片大小 */
  const width = option.imgSize?.width || 100;
  /** 图片高度 */
  const height = option.imgSize?.height || 100;

  // 添加表头信息
  let thead = `<thead>${option.insertHeader || ""}<tr>`;
  for (let i = 0; i < headLength; i++) {
    thead += `<th>${option.header[i]}</th>`;
  }
  thead += "</tr></thead>";

  // 添加每一行数据
  let tbody = "<tbody>";

  for (let i = 0; i < tableLength; i++) {
    tbody += "<tr>";
    const rows = option.data[i];
    for (let j = 0; j < rows.length; j++) {
      const row = rows[j];
      // 如果为图片，则需要加 div包住图片
      if (row && reg.test(row.toString())) {
        tbody += `<td style="width: ${width}px; height: ${height}px; text-align: center; vertical-align: middle">
                    <div style="display: inline">
                        <img src="${row}" width="${width}" height="${height}">
                    </div>
                </td>`;
      } else {
        tbody += `<td style="text-align: ${
          option.textAlign || "left"
        }">${row}</td>`;
      }
    }
    tbody += "</tr>";
  }

  tbody += "</tbody>";

  const ctx = {
    worksheet: option.fileName,
    table: thead + tbody,
  };
  // return console.log(ctx);

  // 编码要用`utf-8`不然默认`gbk`会出现中文乱码
  const prefix = "data:application/vnd.ms-excel;base64,";
  const template = `<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><meta charset="UTF-8"><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>`;

  function base64(val: string) {
    return window.btoa(unescape(encodeURIComponent(val)));
    // return window.btoa(decodeURIComponent(encodeURIComponent(val)));
  }

  function format(value: string, info: any) {
    return value.replace(/{(\w+)}/g, (m, p) => {
      return info[p];
    });
  }

  // 创建下载
  const label = document.createElement("a");
  label.setAttribute("href", `${prefix}` + base64(format(template, ctx)));
  label.setAttribute("download", option.fileName);
  document.body.appendChild(label);
  label.click();
  label.remove();
}
