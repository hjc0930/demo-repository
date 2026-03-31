import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import remarkMath from "remark-math";
import rehypeHighlight from "rehype-highlight";
import rehypeKatex from "rehype-katex";
import "github-markdown-css/github-markdown-light.css";
import "highlight.js/styles/github-dark.css";
import "./MarkdownRenderer.css";
// import "highlight.js/styles/github.css";
import "katex/dist/katex.min.css";

// import "github-markdown-css/github-markdown-dark.css";
// import "highlight.js/styles/github-dark.css";
// import "./MarkdownRenderer.css";

interface MarkdownRendererProps {
  content: string;
}

const sampleMarkdown = `
# Markdown 渲染示例

这是一个 **react-markdown** 的示例组件，支持以下功能：

## 功能特性

- 支持 GFM（GitHub Flavored Markdown）
- 代码高亮
- 表格支持
- 任务列表
- 数学公式

## 代码示例

\`\`\`typescript
interface User {
  id: number
  name: string
  email: string
}

function greet(user: User): string {
  return \`Hello, \${user.name}!\`
}
\`\`\`

## 表格示例

| 功能 | 支持状态 |
|------|----------|
| 标题 | ✅ |
| 列表 | ✅ |
| 代码块 | ✅ |
| 表格 | ✅ |

## 任务列表

- [x] 安装依赖
- [x] 创建组件
- [ ] 添加样式
- [ ] 完成项目

> 提示：你可以传入自定义的 markdown 内容来渲染

## 数学公式示例

行内公式：质能方程 $E = mc^2$，欧拉公式 $e^{i\\pi} + 1 = 0$

块级公式 - 二次方程求根公式：

$$
x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}
$$

块级公式 - 高斯积分：

$$
\\int_{-\\infty}^{\\infty} e^{-x^2} dx = \\sqrt{\\pi}
$$

块级公式 - 麦克斯韦方程组：

$$
\\nabla \\cdot \\mathbf{E} = \\frac{\\rho}{\\varepsilon_0}
$$

## 链接

访问 [React Markdown 文档](https://github.com/remarkjs/react-markdown) 了解更多。
`;

export default function MarkdownRenderer({
  content = sampleMarkdown,
}: MarkdownRendererProps) {
  return (
    <div className="markdown-body">
      <ReactMarkdown
        remarkPlugins={[remarkGfm, remarkMath]}
        rehypePlugins={[rehypeHighlight, rehypeKatex]}
      >
        {content}
      </ReactMarkdown>
    </div>
  );
}
