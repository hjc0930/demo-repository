import { createContext } from "react";
import { FormStore } from "./useForm";

const FormContext = createContext<FormStore | null>(null);

export default FormContext;
