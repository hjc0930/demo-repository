import { Button, Table, Form, Input } from "antd";
import { observer } from "mobx-react-lite";
import {  useEffect } from "react";
import useGlobalStore from "../hooks/useGlobalStore";

const User = () => {
  const userStore = useGlobalStore("user");

  useEffect(() => {
    void (async () => {
      try {
        await userStore.init();
      } catch (error) {
        console.log(error);
      }
    })();
  }, []);

  return (
    <>
      <Button
        type="primary"
        style={{ marginBottom: "16px" }}
        onClick={() => userStore.init()}
      >
        Reset
      </Button>
      <Form
        layout="inline"
        style={{ marginBottom: "16px" }}
        autoComplete="off"
        onFinish={(values) => {
          userStore.search(values);
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
        loading={userStore.loading}
        dataSource={userStore.userInof}
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
};

export default observer(User);
