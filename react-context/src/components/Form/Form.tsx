import FormContext from "./context";
import { FormProps } from "./types";
import useForm from "./useForm";

const Form = <T extends any>(props: FormProps<T>) => {
  const { children, form, onSubmit: externalSubmit, className, style } = props;
  const [formInstance] = useForm(form);

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    externalSubmit?.({} as any);
  };

  const content = (
    <FormContext.Provider value={formInstance}>
      <form onSubmit={(e) => onSubmit(e)} className={className} style={style}>
        {children}
      </form>
    </FormContext.Provider>
  );

  return content;
};

export default Form;
