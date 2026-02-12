import { useState } from "react";
import { ConfigProvider, theme, Layout, Typography, Tabs, Space } from "antd";
import zhCN from "antd/locale/zh_CN";
import Counter from "@/components/Counter";
import UserForm from "./components/UserForm";
import UserList from "./components/UserList";
import TodoList from "./components/TodoList";
import "./index.css";

const { Header, Content, Sider } = Layout;
const { Title, Paragraph } = Typography;

function App() {
  const [activeTab, setActiveTab] = useState("counter");
  const [userFormKey, setUserFormKey] = useState(0);
  const [userListRefresh, setUserListRefresh] = useState(0);

  const handleUserSubmit = async (values: any) => {
    console.log("用户提交:", values);
    setUserListRefresh((prev) => prev + 1);
  };

  const handleEditUser = (user: any) => {
    console.log("编辑用户:", user);
  };

  const tabItems = [
    {
      key: "counter",
      label: "计数器组件",
      children: (
        <Space orientation="vertical" size="large" style={{ width: "100%" }}>
          <Title level={3}>Counter 组件示例</Title>
          <Paragraph>
            展示带有最小/最大值限制的计数器组件，支持增、减、重置操作。
          </Paragraph>
          <Counter />
          <Counter initialValue={50} max={100} min={0} />
        </Space>
      ),
    },
    {
      key: "userForm",
      label: "用户表单",
      children: (
        <Space orientation="vertical" size="large" style={{ width: "100%" }}>
          <Title level={3}>UserForm 组件示例</Title>
          <Paragraph>展示带有表单验证的用户注册/编辑表单。</Paragraph>
          <UserForm
            key={userFormKey}
            onSubmit={handleUserSubmit}
            submitText="注册用户"
          />
        </Space>
      ),
    },
    {
      key: "userList",
      label: "用户列表",
      children: (
        <Space orientation="vertical" size="large" style={{ width: "100%" }}>
          <Title level={3}>UserList 组件示例</Title>
          <Paragraph>展示用户列表表格，支持编辑和删除操作。</Paragraph>
          <UserList onEdit={handleEditUser} refreshTrigger={userListRefresh} />
        </Space>
      ),
    },
    {
      key: "todoList",
      label: "待办事项",
      children: (
        <Space orientation="vertical" size="large" style={{ width: "100%" }}>
          <Title level={3}>TodoList 组件示例</Title>
          <Paragraph>
            展示待办事项列表，支持添加、完成状态切换、删除操作。
          </Paragraph>
          <TodoList />
        </Space>
      ),
    },
  ];

  return (
    <ConfigProvider
      locale={zhCN}
      theme={{
        algorithm: theme.defaultAlgorithm,
      }}
    >
      <Layout style={{ minHeight: "100vh" }}>
        <Header
          style={{
            display: "flex",
            alignItems: "center",
            background: "#001529",
          }}
        >
          <Title level={3} style={{ color: "white", margin: 0 }}>
            测试演示项目
          </Title>
        </Header>
        <Layout>
          <Sider width={250} style={{ background: "#fff" }}>
            <div style={{ padding: "24px" }}>
              <Title level={5}>技术栈</Title>
              <ul style={{ lineHeight: "2" }}>
                <li>React 18</li>
                <li>TypeScript</li>
                <li>Vite</li>
                <li>Ant Design</li>
                <li>Axios</li>
                <li>Vitest</li>
                <li>MSW</li>
              </ul>
              <Title level={5}>运行测试</Title>
              <code>npm test</code>
            </div>
          </Sider>
          <Content style={{ padding: "24px", background: "#f0f2f5" }}>
            <div
              style={{
                background: "#fff",
                padding: "24px",
                borderRadius: "8px",
              }}
            >
              <Title level={2}>测试场景演示</Title>
              <Paragraph>
                本项目展示了各种测试场景，包括组件测试、Hook
                测试、服务层测试和工具函数测试。
              </Paragraph>
              <Tabs
                activeKey={activeTab}
                onChange={setActiveTab}
                items={tabItems}
              />
            </div>
          </Content>
        </Layout>
      </Layout>
    </ConfigProvider>
  );
}

export default App;
