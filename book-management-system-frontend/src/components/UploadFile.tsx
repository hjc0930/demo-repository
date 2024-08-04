import { PlusOutlined } from "@ant-design/icons";
import { Upload, Image } from "antd";
import type { UploadChangeParam, UploadFile } from "antd/es/upload";
import { useState } from "react";

interface UploadFileProps {
  value?: string;
  onChange?: (value: string) => void;
}

const uploadButton = (
  <button style={{ border: 0, background: "none" }} type="button">
    <PlusOutlined />
    <div style={{ marginTop: 8 }}>Upload</div>
  </button>
);

const UploadFile = (props: UploadFileProps) => {
  const { value, onChange } = props;
  const [previewOpen, setPreviewOpen] = useState(false);

  const onUploadChange = (event: UploadChangeParam<UploadFile>) => {
    if (event.file.status === "done") {
      onChange?.(event.file.response);
    }
  };

  return value ? (
    <Image
      src={`http://localhost:3000/${value}`}
      width={200}
      preview={{
        visible: previewOpen,
        onVisibleChange: (visible) => setPreviewOpen(visible),
      }}
    />
  ) : (
    <Upload
      action="http://localhost:3000/book/upload"
      method="post"
      listType="picture-card"
      onChange={onUploadChange}
    >
      {uploadButton}
    </Upload>
  );
};

export default UploadFile;
