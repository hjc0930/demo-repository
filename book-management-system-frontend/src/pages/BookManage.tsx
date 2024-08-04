import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
  SnippetsOutlined,
} from "@ant-design/icons";
import { useRequest } from "ahooks";
import {
  Button,
  Card,
  Empty,
  Flex,
  Form,
  Input,
  message,
  Modal,
  Space,
  Spin,
  Tooltip,
  Typography,
  Upload,
  Popconfirm,
} from "antd";
import {
  getBookManage,
  deleteBookManage,
  createBookManage,
  editBookManage,
} from "../services/bookManage";
import "./book-manage.css";
import { useState } from "react";
import UploadFile from "../components/UploadFile";

const { Paragraph } = Typography;

function BookManage() {
  const { data, loading, run } = useRequest(getBookManage);
  const createRequest = useRequest(createBookManage, { manual: true });
  const editRequest = useRequest(editBookManage, { manual: true });
  const deletedRequest = useRequest(deleteBookManage, { manual: true });

  const modalLoading = editRequest.loading || createRequest.loading;

  const [open, setOpen] = useState(false);
  const [detailOpen, setDetailOpen] = useState(false);
  const [form] = Form.useForm();
  const [api, contextHolder] = message.useMessage();

  const onSearch = (name: string) => {
    run(name);
  };

  const onDeleted = async (id: number) => {
    await deletedRequest.runAsync(id);
    api.success("删除成功");
    run();
  };

  const onSubmit = async () => {
    const values = await form.validateFields();

    if (values?.id) {
      await editRequest.runAsync({
        ...values,
      });
      api.success("更新成功");
    } else {
      await createRequest.runAsync({
        ...values,
      });
      api.success("新增成功");
    }

    run();
    setOpen(false);
  };

  const onEdit = (item: {
    id: number;
    author: string;
    name: string;
    description: string;
    cover: string;
  }) => {
    form.setFieldsValue(item);
    setOpen(true);
  };

  const onDetail = (item: {
    id: number;
    author: string;
    name: string;
    description: string;
    cover: string;
  }) => {
    setDetailOpen(true);
    form.setFieldsValue(item);
  };

  return (
    <>
      {contextHolder}
      <h3>图书管理系统</h3>
      <Space>
        <Input.Search placeholder="Enter" onSearch={onSearch} />
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setOpen(true)}
        >
          New Book
        </Button>
      </Space>

      {data?.length === 0 ? (
        <Empty />
      ) : (
        <Spin spinning={loading}>
          <Flex
            gap={14}
            align="start"
            style={{
              padding: "0.8rem",
            }}
          >
            {/* "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png" */}
            {data?.map((item) => (
              <Card
                key={item.id}
                hoverable
                style={{ width: 240 }}
                cover={
                  <img
                    alt="cover"
                    src={`http://localhost:3000/${item.cover}`}
                  />
                }
                actions={[
                  <Tooltip title="详情">
                    <SnippetsOutlined
                      key="snippets"
                      onClick={() => {
                        onDetail(item);
                      }}
                    />
                  </Tooltip>,
                  <Tooltip title="编辑">
                    <EditOutlined key="edit" onClick={() => onEdit(item)} />
                  </Tooltip>,
                  <Tooltip title="删除">
                    <Popconfirm
                      title="Delete a book"
                      description="Are you sure to delete this book?"
                      onConfirm={() => onDeleted(item.id)}
                      okText="Yes"
                      cancelText="No"
                    >
                      <DeleteOutlined key="ellipsis" />
                    </Popconfirm>
                  </Tooltip>,
                ]}
              >
                <Card.Meta
                  title={
                    <>
                      {item.name}
                      <span
                        style={{
                          fontSize: "0.6rem",
                          color: "rgba(0, 0, 0, 0.45)",
                        }}
                      >
                        -{item.author}
                      </span>
                    </>
                  }
                  description={
                    <Paragraph
                      ellipsis={{ rows: 3, expandable: true, symbol: "more" }}
                    >
                      {item.description}
                    </Paragraph>
                  }
                />
              </Card>
            ))}
          </Flex>
        </Spin>
      )}
      <Modal
        title={form.getFieldValue("id") ? "编辑图书" : "新增图书"}
        open={open}
        okText="Submit"
        afterClose={() => form.resetFields()}
        okButtonProps={{
          loading: modalLoading,
        }}
        onOk={onSubmit}
        onCancel={() => setOpen(false)}
      >
        <Form form={form} layout="vertical" autoComplete="off">
          <Form.Item label={null} hidden name="id">
            <Input />
          </Form.Item>
          <Form.Item
            label="图书名称"
            name="name"
            rules={[{ required: true, message: "请输入图书名称" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="作者"
            name="author"
            rules={[{ required: true, message: "请输入作者" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="描述"
            name="description"
            rules={[{ required: true, message: "请输入描述" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="封面"
            name="cover"
            rules={[{ required: true, message: "请输入封面" }]}
            initialValue=""
          >
            <UploadFile />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="详情"
        open={detailOpen}
        onCancel={() => setDetailOpen(false)}
        footer={null}
        afterClose={() => form.resetFields()}
      >
        <Form form={form} layout="vertical" autoComplete="off">
          <Form.Item label={null} hidden name="id">
            <Input readOnly />
          </Form.Item>
          <Form.Item
            label="图书名称"
            name="name"
            rules={[{ required: true, message: "请输入图书名称" }]}
          >
            <Input readOnly />
          </Form.Item>
          <Form.Item
            label="作者"
            name="author"
            rules={[{ required: true, message: "请输入作者" }]}
          >
            <Input readOnly />
          </Form.Item>
          <Form.Item
            label="描述"
            name="description"
            rules={[{ required: true, message: "请输入描述" }]}
          >
            <Input readOnly />
          </Form.Item>
          <Form.Item
            label="封面"
            name="cover"
            rules={[{ required: true, message: "请输入封面" }]}
            initialValue=""
          >
            <UploadFile />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}

export default BookManage;
