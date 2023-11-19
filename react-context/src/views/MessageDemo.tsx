import messageInstance from "../components/Message";

export function MessageDemo() {
const onOpen = () => {
// const framework = document.createDocumentFragment();
// document.body.appendChild(framework);
// framework.appendChild()
// message.success(123123)
// createRoot(framework).render(<div>123123</div>);
messageInstance({
content: "",
});
};

return (
<div>
<h2>Message</h2>
<button onClick={onOpen}>Open</button>
</div>
);
}
