# React

## 目录结构

```bash
.
├── public           // 静态资源
├── scripts          // 项目配置文件
├── src
│   ├── assets       // 静态资源
│   ├── components   // 公共组件
│   ├── hooks        // 公共hooks
│   ├── layouts      // 公共布局
│   ├── routers      // 路由
│   ├── services     // 接口聚合
│   ├── stores       // 全局状态
│   ├── types        // 公共声明文件
│   ├── utils        // 公共工具函数
│   └── views        // 页面组件
│   └── App.tsx      // 根组件
│   └── index.tsx    // 入口文件
├── global.d.ts      // 全局资源声明文件
├── index.html       // 单页html文件
├── package.json     // 依赖声明文件
└── tsconfig.json
```

## git commit 信息提交规范

编写提交信息时，统一以"提交类型: 提交信息"这样的格式编写，项目会检测 git 信息的提交是否符合规范，不符合规范的会提交失败

例子：

```bash
git commit -m "feat: new component"
```

提交类型：

| 类型     | 描述                                               |
| -------- | -------------------------------------------------- |
| feat     | 新功能                                             |
| refactor | 代码重构                                           |
| style    | 代码格式更改                                       |
| perf     | 代码优化相关，如提升性能，体验                     |
| docs     | 文档更改                                           |
| test     | 测试用例相关修改                                   |
| ci       | 持续集成修改                                       |
| revert   | 版本回滚                                           |
| build    | 编译相关的修改，例如对项目构建或者构建依赖的改动   |
| chore    | 其他修改, 比如改变构建流程、或者增加依赖库、工具等 |
