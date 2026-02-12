# ARIA 角色与单元测试指南

> 本文档介绍 ARIA 角色概念，以及如何在单元测试中合理使用 `getByRole` 和 `getByText`。

## 目录

- [什么是 ARIA](#什么是-aria)
- [ARIA 角色对照表](#aria-角色对照表)
- [getByRole vs getByText](#getbyrole-vs-getbytext)
- [测试场景实战](#测试场景实战)
- [如何查找元素角色](#如何查找元素角色)
- [最佳实践](#最佳实践)
- [常见问题](#常见问题)

---

## 什么是 ARIA

**ARIA**（Accessible Rich Internet Applications）是一套 Web 无障碍标准，定义了如何让屏幕阅读器等辅助技术理解网页内容。

**ARIA 角色（Role）** 就是给元素贴上"语义标签"，告诉浏览器/屏幕阅读器"这个元素是什么"，而不管它用什么 HTML 标签实现。

### 为什么需要 ARIA 角色？

```tsx
// 问题场景：用 div 实现按钮
<div onClick={handleClick}>点击我</div>
// 屏幕阅读器：这是个普通的 div，不知道可点击

// 解决方案 1：用语义化标签（推荐）
<button onClick={handleClick}>点击我</button>
// role="button" 自动获得

// 解决方案 2：给 div 加 role
<div role="button" onClick={handleClick}>点击我</div>
// 屏幕阅读器：这是个按钮
```

---

## ARIA 角色对照表

### 交互元素类

| 角色 | 对应 HTML 元素 | 语义 |
|------|----------------|------|
| `button` | `<button>` | 可点击按钮 |
| `link` | `<a href>` | 超链接 |
| `menuitem` | 无原生对应 | 菜单项 |
| `tab` | 无原生对应 | 标签页 |

### 表单输入类

| 角色 | 对应 HTML 元素 | 语义 |
|------|----------------|------|
| `textbox` | `<input type="text">` | 文本输入框 |
| `searchbox` | `<input type="search">` | 搜索框 |
| `checkbox` | `<input type="checkbox">` | 复选框 |
| `radio` | `<input type="radio">` | 单选框 |
| `combobox` | `<select>` | 下拉选择 |
| `slider` | `<input type="range">` | 滑块 |

### 内容展示类

| 角色 | 对应 HTML 元素 | 语义 |
|------|----------------|------|
| `heading` | `<h1>`~`<h6>` | 标题 |
| `img` | `<img>` | 图片 |
| `list` | `<ul>`, `<ol>` | 列表 |
| `listitem` | `<li>` | 列表项 |
| `table` | `<table>` | 表格 |
| `row` | `<tr>` | 表格行 |
| `cell` | `<td>` | 表格单元格 |
| `columnheader` | `<th>` | 列标题 |
| `article` | `<article>` | 文章 |
| `definition` | `<dfn>` | 定义 |

### 导航类

| 角色 | 对应 HTML 元素 | 语义 |
|------|----------------|------|
| `navigation` | `<nav>` | 导航区域 |
| `menu` | 无原生对应 | 菜单 |

### 反馈类

| 角色 | 对应 HTML 元素 | 语义 |
|------|----------------|------|
| `dialog` | `<dialog>` | 弹窗 |
| `alert` | 无原生对应 | 警告提示 |
| `tooltip` | 无原生对应 | 提示框 |
| `status` | 无原生对应 | 状态提示 |
| `progressbar` | `<progress>` | 进度条 |

---

## getByRole vs getByText

### 核心区别

| 方法 | 查询依据 | 适用场景 |
|------|----------|----------|
| `getByRole` | **ARIA 角色** | 按钮、链接、表单等交互元素 |
| `getByText` | **文本内容** | 纯展示文本 |

### 实际对比

```tsx
// 组件代码
<button>重置</button>
<input placeholder="用户名" />
<a href="/home">首页</a>

// getByRole - 按角色查询
screen.getByRole("button", { name: "重置" })
screen.getByRole("textbox", { name: "用户名" })
screen.getByRole("link", { name: "首页" })

// getByText - 按文本查询
screen.getByText("重置")
screen.getByText("用户名")  // 可能匹配到 placeholder
screen.getByText("首页")
```

### 优缺点

#### getByRole 的优势

| 优势 | 说明 |
|------|------|
| **更贴近用户** | 用户通过"点击按钮"而非"点击文本"来交互 |
| **更稳定** | 文本改了但按钮还是按钮 |
| **无障碍友好** | 鼓励写可访问的组件 |
| **语义清晰** | 明确表达在找什么元素 |
| **避免误匹配** | 通过 role 区分相同文本的不同元素 |

#### getByText 的劣势

```tsx
// getByText 容易误匹配
<div>
  <span>删除</span>
  <button>删除</button>  // 想点这个，但可能点到 span
</div>

// getByRole 精准定位
screen.getByRole("button", { name: "删除" })
```

---

## 测试场景实战

### 场景 1：基础按钮/链接

```tsx
// 组件
<button onClick={handleSubmit}>提交</button>
<a href="/home">首页</a>

// 测试
test('用户可以提交表单', () => {
  screen.getByRole("button", { name: "提交" })
  screen.getByRole("link", { name: "首页" })
})
```

### 场景 2：图标按钮（无文本）

```tsx
// 组件 - 必须添加 aria-label
<button aria-label="关闭对话框" onClick={onClose}>
  <CloseIcon />
</button>

// 测试
test('用户可以关闭对话框', () => {
  screen.getByRole("button", { name: "关闭对话框" })
})
```

### 场景 3：表单元素

```tsx
// 组件
<label htmlFor="username">用户名</label>
<input id="username" type="text" placeholder="请输入用户名" />

// 测试 - 方式1：通过 label（推荐）
screen.getByLabelText("用户名")

// 测试 - 方式2：通过 role + name
screen.getByRole("textbox", { name: "用户名" })
```

### 场景 4：Antd 组件（中文空格问题）

```tsx
// Antd Button - 会在中文字符间插入空格
<Button type="primary">提交</Button>
// 实际渲染：<button><span>提 交</span></button>

// ✅ 推荐：使用 getByRole
screen.getByRole("button", { name: "提交" })

// ⚠️ 备选：使用正则表达式处理空格
screen.getByText(/提\s*交/)

// ❌ 不推荐：直接匹配会失败
screen.getByText("提交")  // 失败！
```

### 场景 5：复选框/单选框

```tsx
// 组件
<label>
  <input type="checkbox" name="agree" />
  我同意服务条款
</label>

// 测试
test('用户可以同意条款', () => {
  const checkbox = screen.getByRole("checkbox", { name: "我同意服务条款" })
  expect(checkbox).not.toBeChecked()

  fireEvent.click(checkbox)
  expect(checkbox).toBeChecked()
})
```

### 场景 6：下拉选择

```tsx
// 组件
<select>
  <option value="">请选择</option>
  <option value="apple">苹果</option>
  <option value="banana">香蕉</option>
</select>

// 测试
test('用户可以选择水果', () => {
  screen.getByRole("combobox", { name: /请选择/ })

  // 展开后选择
  fireEvent.mouseDown(screen.getByRole("combobox"))
  screen.getByRole("option", { name: "苹果" })
})
```

### 场景 7：弹窗/对话框

```tsx
// 组件
<Dialog open={true} aria-labelledby="dialog-title">
  <DialogTitle id="dialog-title">确认删除</DialogTitle>
  <DialogContent>确定要删除这条记录吗？</DialogContent>
  <DialogActions>
    <Button>取消</Button>
    <Button>确定</Button>
  </DialogActions>
</Dialog>

// 测试
test('显示删除确认弹窗', () => {
  screen.getByRole("dialog", { name: "确认删除" })
  screen.getByRole("button", { name: "确定" })
  screen.getByRole("button", { name: "取消" })
})
```

### 场景 8：导航菜单

```tsx
// 组件
<nav aria-label="主导航">
  <ul>
    <li><a href="/home">首页</a></li>
    <li><a href="/products">产品</a></li>
    <li><a href="/about">关于</a></li>
  </ul>
</nav>

// 测试
test('主导航包含所有链接', () => {
  const nav = screen.getByRole("navigation", { name: "主导航" })
  expect(nav).toBeInTheDocument()

  screen.getByRole("link", { name: "首页" })
  screen.getByRole("link", { name: "产品" })
  screen.getByRole("link", { name: "关于" })
})
```

### 场景 9：表格

```tsx
// 组件
<table>
  <thead>
    <tr>
      <th scope="col">姓名</th>
      <th scope="col">年龄</th>
      <th scope="col">操作</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>张三</td>
      <td>25</td>
      <td><button>编辑</button></td>
    </tr>
  </tbody>
</table>

// 测试
test('表格显示用户数据', () => {
  screen.getByRole("table")
  screen.getByRole("columnheader", { name: "姓名" })
  screen.getByRole("row", { name: "张三 25 编辑" })
  screen.getByRole("cell", { name: "张三" })
  screen.getByRole("button", { name: "编辑" })
})
```

### 场景 10：警告/提示信息

```tsx
// 组件
<div role="alert" aria-live="assertive">
  操作成功！数据已保存。
</div>

// 测试
test('显示成功提示', () => {
  screen.getByRole("alert", { name: /操作成功/ })
})
```

### 场景 11：标签页

```tsx
// 组件
<div role="tablist">
  <button role="tab" aria-selected="true">概览</button>
  <button role="tab" aria-selected="false">详情</button>
  <button role="tab" aria-selected="false">设置</button>
</div>

// 测试
test('标签页切换', () => {
  screen.getByRole("tab", { name: "概览", selected: true })
  screen.getByRole("tab", { name: "详情", selected: false })
})
```

### 场景 12：进度条

```tsx
// 组件
<progress value="50" max="100" aria-label="上传进度">50%</progress>

// 测试
test('显示上传进度', () => {
  const progress = screen.getByRole("progressbar", { name: "上传进度" })
  expect(progress).toHaveValue(50)
})
```

---

## 如何查找元素角色

### 方法 1：浏览器 DevTools

```
1. 打开 Chrome DevTools（F12）
2. 选中元素
3. 切换到 Accessibility 面板
4. 查看 Computed role（计算出的角色）
```

### 方法 2：Testing Library 调试工具

```tsx
// 列出页面上所有角色
import { screen } from '@testing-library/react'

screen.logRoles(document.body)

// 输出示例：
// button:
//   - "提交"
//   - "取消"
// link:
//   - "首页"
// textbox:
//   - "用户名"
```

### 方法 3：使用 @testing-library/dom

```tsx
import { within } from '@testing-library/dom'

// 查看特定容器内的所有角色
const container = screen.getByTestId('form-container')
within(container).getByRole('textbox')
```

---

## 最佳实践

### Testing Library 查询优先级

按照官方推荐，从高到低：

```tsx
// 1. 优先级最高：getByRole
screen.getByRole("button", { name: "提交" })

// 2. 其次：getByLabelText（表单元素）
screen.getByLabelText("用户名")

// 3. 然后：getByPlaceholderText（仅当没有 label 时）
screen.getByPlaceholderText("请输入用户名")

// 4. 然后：getByText（纯文本内容）
screen.getByText("欢迎回来")

// 5. 然后：getByDisplayValue（表单当前值）
screen.getByDisplayValue("已输入的内容")

// 6. 最后手段：getByTestId（需要添加 data-testid）
screen.getByTestId("submit-button")
```

### 测试场景决策树

```
需要测试一个元素？
│
├─ 是可交互元素吗？
│  ├─ 是 → 使用 getByRole
│  │  ├─ 按钮/链接？→ role="button" / role="link"
│  │  ├─ 表单输入？→ role="textbox" / role="checkbox"
│  │  ├─ 下拉选择？→ role="combobox"
│  │  └─ 模态框？→ role="dialog"
│  │
│  └─ 否 → 判断是否为表单元素
│     ├─ 有 label？→ getByLabelText
│     └─ 无 label？→ 考虑 getByText
│
└─ 是纯展示文本？
   └─ 使用 getByText
```

### 编写无障碍组件的原则

| 原则 | 说明 | 示例 |
|------|------|------|
| **优先语义化标签** | 用原生 HTML 元素 | 用 `<button>` 而非 `<div>` |
| **图标按钮加 label** | 纯图标必须加说明 | `<button aria-label="关闭">` |
| **表单要有 label** | 输入框关联标签 | `<label htmlFor="id">` |
| **使用 ARIA 属性** | 增强语义 | `aria-selected`, `aria-expanded` |
| **测试用 role** | 模拟真实用户行为 | `getByRole("button")` |
| **避免过度依赖 text** | 文本会变，角色不变 | 优先 `getByRole` |

---

## 常见问题

### Q1: 什么时候用 getByText？

**答**: 以下场景适合用 `getByText`：
- 纯展示的文本内容（标题、段落）
- 验证页面是否包含某些文本
- 没有明显交互角色的元素

```tsx
// ✅ 适合的场景
expect(screen.getByText("操作成功")).toBeInTheDocument()
expect(screen.getByRole("heading", { name: "欢迎使用" })).toBeInTheDocument()
```

### Q2: Antd 组件中文空格问题如何处理？

**答**: 推荐使用 `getByRole`：

```tsx
// ✅ 最佳方案
screen.getByRole("button", { name: "重置" })

// ✅ 备选方案
screen.getByText(/重\s*置/)
screen.getByText("重置", { exact: false })
```

### Q3: 多个相同名称的元素怎么办？

**答**: 使用更具体的选择器或 `getAllByRole`：

```tsx
// 多个"删除"按钮
screen.getAllByRole("button", { name: "删除" })[0]  // 第一个

// 或结合其他属性筛选
within(screen.getByTestId("user-row-1"))
  .getByRole("button", { name: "删除" })
```

### Q4: 自定义组件如何测试？

**答**: 确保组件有正确的 role 和 aria 属性：

```tsx
// 组件
const CustomButton = ({ icon, children }) => (
  <button role="button" aria-label={children}>
    {icon}
  </button>
)

// 测试
screen.getByRole("button", { name: "按钮文字" })
```

### Q5: 什么时候必须用 data-testid？

**答**: 以下场景可以考虑：
- 元素没有明确的 role
- 文本会频繁变化
- 国际化场景，文本不固定
- 第三方组件无法添加 aria 属性

```tsx
// 组件
<div data-testid="user-avatar">...</div>

// 测试
screen.getByTestId("user-avatar")
```

---

## 参考资源

- [ARIA 规范 (W3C)](https://www.w3.org/TR/wai-aria-1.2/)
- [Testing Library 文档](https://testing-library.com/docs/queries/about/#priority)
- [Web 无障碍最佳实践](https://www.w3.org/WAI/WCAG21/quickref/)
- [ARIA 角色速查表](https://www.w3.org/TR/wai-aria-1.2/#role_definitions)
