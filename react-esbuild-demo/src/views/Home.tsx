import React, { useState } from "react";
import { observer } from "mobx-react-lite";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";

const Home: React.FC = () => {
  return <Spin>
    <div>123123</div>
  </Spin>;
};

export default observer(Home);
