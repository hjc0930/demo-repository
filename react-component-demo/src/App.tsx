import { useState } from "react";
import { Input, Form, Rate, Button } from "antd";

const CheckboxGroup = (props: any) => {
  const [value, setValue] = useState(props.value);
  return (
    <ul>
      {["apple", "pear"].map((item) => (
        <li key={item}>
          <button
            style={{
              color: value === item ? "red" : "black",
            }}
            onClick={() => {
              setValue(item);
              props.onChange(item);
            }}
          >
            {item}
          </button>
        </li>
      ))}
    </ul>
    // <Checkbox.Group
    //   value={props.value}
    //   onChange={(e) => {
    //     props.onChange(e);
    //   }}
    //   options={[
    //     {
    //       label: "Apple",
    //       value: "Apple",
    //     },
    //     {
    //       label: "Pear",
    //       value: "Pear",
    //     },
    //   ]}
    // />
  );
};

const App = () => {
  const [form] = Form.useForm();
  return (
    <>
      <Form
        form={form}
        onValuesChange={(values) => {
          console.log(values);
        }}
      >
        <Form.Item name="name" label="Name">
          <Input />
        </Form.Item>
        <Form.Item name="rate" label="Rate">
          <Rate />
        </Form.Item>
      </Form>
      <Button
        onClick={() => {
          console.log(form.getFieldsValue());
        }}
      >
        aaa
      </Button>
    </>
  );
  // return (

  // <Form form={form} onChange={(values) => console.log(values)}>
  //   <FormItem
  //     label="Name"
  //     name="name"
  //     render={(props) => {
  //       return (
  //         <Input
  //           {...props}
  //           onChange={(e) => props.onChange(e.target.value)}
  //         />
  //       );
  //     }}
  //   />
  //   <FormItem
  //     label="Name"
  //     name="age"
  //     propsName="checked"
  //     defaultValue={false}
  //     render={(props) => {
  //       return (
  //         <Checkbox
  //           {...props}
  //           onChange={(e) => {
  //             props.onChange(e.target.checked);
  //           }}
  //         >
  //           Checkbox
  //         </Checkbox>
  //       );
  //     }}
  //   />
  // </Form>
  // );
};

export default App;
