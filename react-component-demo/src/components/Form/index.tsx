import { PropsWithChildren, use } from "react";
import useForm, { FormStore } from "./useForm";
import FormContext from "./context";

export interface FormProps extends PropsWithChildren {
  form?: FormStore;
  onSubmit?: (values: any) => void;
  onReset?: () => void;
  onChange?: (values: any) => void;
}

const Form = (props: FormProps) => {
  const { onSubmit, onReset, onChange, form, children } = props;
  const [formInstance] = useForm(form);

  const onSubmitHandler = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const values = formInstance.getAllFieldValues();

    onSubmit?.(values);
  };
  const onResetHandler = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onReset?.();
  };

  const onChangeHandler = () => {
    const values = formInstance.getAllFieldValues();
    onChange?.(values);
  };

  return (
    <FormContext.Provider value={{ formInstance }}>
      <form
        onSubmit={onSubmitHandler}
        onReset={onResetHandler}
        onChange={onChangeHandler}
      >
        {children}
      </form>
    </FormContext.Provider>
  );
};

export default Form;
