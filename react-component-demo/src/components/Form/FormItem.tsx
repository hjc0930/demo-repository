import { ReactNode, useContext, useEffect, useState } from "react";
import { NamePath } from "./types";
import FormContext from "./context";

export interface FormItemProps {
  label?: ReactNode;
  name?: NamePath;
  defaultValue?: any;
  propsName?: string;
  propsTrigger?: string;
  required?: boolean | { message: string };
  render?: (props: any, record: any) => ReactNode;
}

const FormItem = (props: FormItemProps) => {
  const {
    label,
    name,
    defaultValue = "",
    propsName = "value",
    propsTrigger = "onChange",
    render,
  } = props;
  const { formInstance } = useContext(FormContext);
  const controllerState = useState(defaultValue);

  useEffect(() => {
    if (!name) {
      formInstance?.remove(name!);
      return;
    }
    return () => {
      formInstance?.remove(name);
    };
  }, [name]);

  const onChange = (value: any) => {
    name && formInstance?.setFieldValue(name, value);
    controllerState[1](value);
  };

  const controllerProps = {
    [propsName]: controllerState[0],
    [propsTrigger]: onChange,
  };
  const children = render?.(controllerProps, {});

  if (!children) {
    return null;
  }
  return (
    <div>
      {label ? <label htmlFor={name?.toString()}>{label}</label> : null}
      {children}
    </div>
  );
};

export default FormItem;
