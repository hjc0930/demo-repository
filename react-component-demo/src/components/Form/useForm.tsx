import { NamePath } from "./types";

export class FormStore {
  private instances = new Map<NamePath, any>();

  public setFieldValue = (key: NamePath, value: any) => {
    this.instances.set(key, value);
  };

  public getFieldValue = (key: NamePath) => {
    return this.instances.get(key);
  };

  public getAllFieldValues = () => {
    return this.instances.keys().reduce((acc, key) => {
      acc[key.toString()] = this.instances.get(key);
      return acc;
    }, {} as Record<string, any>);
  };

  public remove = (key: NamePath) => {
    this.instances.delete(key);
  };
}

const useForm = (instance?: FormStore) => {
  const formInstance = instance ?? new FormStore();
  return [formInstance];
};

export default useForm;
