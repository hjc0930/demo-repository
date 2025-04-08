import React from "react";
import { FormStore } from "./useForm";

const FormContext = React.createContext<{ formInstance: FormStore }>({} as any);

export default FormContext;
