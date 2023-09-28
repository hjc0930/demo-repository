import { Button, Table, Form, Input } from "antd";
import { mailSelector, useUserStore } from "../stores/user";
import { memo, useEffect } from "react";
import { shallow } from "zustand/shallow";

const User = memo(() => {
  const { init, loading, userInof, search } = useUserStore((state) => ({
    init: state.init,
    loading: state.loading,
    userInof: state.userInof,
    search: state.search,
  }), shallow);
  const mail = useUserStore(mailSelector, shallow);

  useEffect(() => {
    void (async () => {
      try {
        await init();
      } catch (error) {
        console.log(error);
      }
    })();
  }, []);

  // console.log(mail);

  return (
    <>
      <Button
        type="primary"
        style={{ marginBottom: "16px" }}
        onClick={() => init()}
      >
        Reset
      </Button>
      <Form
        layout="inline"
        style={{ marginBottom: "16px" }}
        autoComplete="off"
        onFinish={(values) => {
          search(values);
        }}
      >
        <Form.Item label="id" name="id">
          <Input placeholder="enter" />
        </Form.Item>
        <Form.Item label="name" name="name">
          <Input placeholder="enter" />
        </Form.Item>
        <Form.Item label="phone" name="phone">
          <Input placeholder="enter" />
        </Form.Item>
        <Form.Item label="main" name="main">
          <Input placeholder="enter" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">
            Search
          </Button>
        </Form.Item>
      </Form>
      <Table
        rowKey="id"
        loading={loading}
        dataSource={userInof}
        columns={[
          {
            title: "Key",
            dataIndex: "id",
          },
          {
            title: "name",
            dataIndex: "name",
          },
          {
            title: "phone",
            dataIndex: "phone",
          },
          {
            title: "mail",
            dataIndex: "mail",
          },
        ]}
      />
    </>
  );
});

export default User;
