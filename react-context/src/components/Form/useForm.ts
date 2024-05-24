import { useRef } from "react";
import { ItemInternal } from "./Item";

export class FormStore {
  private entitys: ItemInternal[] = [];

  public register = (instance: ItemInternal) => {
    this.entitys.push(instance);

    return () => {
      this.entitys = this.entitys.filter((entity) => entity !== instance);
    };
  };

  public getFieldInstance = () => {
    return this.entitys;
  };
}

const useForm = (form?: FormStore): readonly [FormStore] => {
  const formRef = useRef<FormStore>();

  if (!formRef.current) {
    if (form) {
      formRef.current = form;
    } else {
      formRef.current = new FormStore();
    }
  }

  return [formRef.current];
};

export default useForm;
