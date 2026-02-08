import React, { useState } from "react";

const About: React.FC = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  return (
    <>
      <section>
        <form>
          <input
            type="text"
            name="title"
            value={title}
            onChange={(e) => {
              setTitle(e.target.value);
            }}
            placeholder="请输入"
          />
          <input
            type="text"
            name="content"
            value={content}
            onChange={(e) => {
              setContent(e.target.value);
            }}
            placeholder="请输入"
          />
          <input type="submit" value="Sumbit" />
        </form>
      </section>
      <section>
        <h2>Posts</h2>
      </section>
    </>
  );
};
export default About;
