import { PropsWithChildren, ReactNode } from "react";
import { FormStore } from "./useForm";

export type NameType = string | number | (string | number)[];

export interface ItemProps extends PropsWithChildren {
  label?: ReactNode;
  name?: NameType;
  shouldUpdate?: boolean;
}

type InternalFormProps = Omit<
  React.DetailedHTMLProps<
    React.FormHTMLAttributes<HTMLFormElement>,
    HTMLFormElement
  >,
  "onSubmit"
> &
  PropsWithChildren;

export interface FormProps<T = any> extends InternalFormProps {
  form?: FormStore;
  onSubmit?: (values: T) => void;
  // name?: NameType;
}
