import { render, screen, fireEvent, waitFor } from "@testing-library/react";

import { Dropdown } from "antd";

test("Dropdown", async () => {
  render(
    <Dropdown
      menu={{
        items: [{ key: "1", label: <span>Dropdown Menu Item</span> }],
      }}
    >
      <button>Click me</button>
    </Dropdown>
  );
  const triggerEle = screen.getByText(/click me/i);
  fireEvent.mouseEnter(triggerEle);

  await waitFor(() => {
    expect(screen.getByText(/Dropdown Menu Item/i)).toBeInTheDocument();
  });
});
