import {
  Component,
  ReactElement,
  ReactNode,
  cloneElement,
  useContext,
} from "react";
import { ItemProps } from "./types";
import useForm, { FormStore } from "./useForm";
import FormContext from "./context";
import { getNamePath } from "./utils";

// eslint-disable-next-line @typescript-eslint/consistent-indexed-object-style
interface ChildProps {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  [name: string]: any;
}

export class ItemInternal extends Component<
  ItemProps & {
    name?: (string | number)[];
    formContext: null | FormStore;
  }
> {
  private mounted = false;
  private cancelRegisterFunc: null | (() => void) = null;

  constructor(
    props: ItemProps & {
      formContext: FormStore;
    }
  ) {
    super(props);
  }

  componentDidMount(): void {
    const { shouldUpdate, formContext } = this.props;

    this.mounted = true;

    // Register on init
    if (formContext) {
      const { register } = formContext;
      this.cancelRegisterFunc = register(this);
    }

    // One more render for component in case fields not ready
    if (shouldUpdate === true) {
      this.reRender();
    }
  }

  public componentWillUnmount() {
    this.cancelRegister();
    // this.triggerMetaEvent(true);
    this.mounted = false;
  }

  public cancelRegister = () => {
    if (this.cancelRegisterFunc) {
      this.cancelRegisterFunc();
      this.cancelRegisterFunc = null;
    }
  };

  public reRender() {
    if (!this.mounted) {
      return;
    }
    this.forceUpdate();
  }

  public getNamePath = () => {
    const { name } = this.props;

    return name !== undefined ? [...name] : [];
  };

  public getControlled = (childProps: ChildProps = {}) => {
    const {
      name,
      trigger,
      validateTrigger,
      getValueFromEvent,
      normalize,
      valuePropName,
      getValueProps,
      fieldContext,
    } = this.props;

    const mergedValidateTrigger =
      validateTrigger !== undefined
        ? validateTrigger
        : fieldContext.validateTrigger;

    const namePath = this.getNamePath();
    const { getInternalHooks, getFieldsValue } = fieldContext;
    const { dispatch } = getInternalHooks(HOOK_MARK);
    const value = this.getValue();
    const mergedGetValueProps =
      getValueProps || ((val: StoreValue) => ({ [valuePropName]: val }));

    const originTriggerFunc = childProps[trigger];

    const valueProps = name !== undefined ? mergedGetValueProps(value) : {};

    // warning when prop value is function
    if (process.env.NODE_ENV !== "production" && valueProps) {
      Object.keys(valueProps).forEach((key) => {
        warning(
          typeof valueProps[key] !== "function",
          `It's not recommended to generate dynamic function prop by \`getValueProps\`. Please pass it to child component directly (prop: ${key})`
        );
      });
    }

    const control = {
      ...childProps,
      ...valueProps,
    };

    // Add trigger
    control[trigger] = (...args: any[]) => {
      // Mark as touched
      this.touched = true;
      this.dirty = true;

      this.triggerMetaEvent();

      let newValue: StoreValue;
      if (getValueFromEvent) {
        newValue = getValueFromEvent(...args);
      } else {
        newValue = defaultGetValueFromEvent(valuePropName, ...args);
      }

      if (normalize) {
        newValue = normalize(newValue, value, getFieldsValue(true));
      }

      dispatch({
        type: "updateValue",
        namePath,
        value: newValue,
      });

      if (originTriggerFunc) {
        originTriggerFunc(...args);
      }
    };

    // Add validateTrigger
    const validateTriggerList: string[] = toArray(mergedValidateTrigger || []);

    validateTriggerList.forEach((triggerName: string) => {
      // Wrap additional function of component, so that we can get latest value from store
      const originTrigger = control[triggerName];
      control[triggerName] = (...args: EventArgs) => {
        if (originTrigger) {
          originTrigger(...args);
        }

        // Always use latest rules
        const { rules } = this.props;
        if (rules && rules.length) {
          // We dispatch validate to root,
          // since it will update related data with other field with same name
          dispatch({
            type: "validateField",
            namePath,
            triggerName,
          });
        }
      };
    });

    return control;
  };

  render(): ReactNode {
    const { label, name } = this.props;
    const namePath = Array.isArray(name) ? name.join("_") : name?.toString();
    const children = this.props.children as ReactElement;
    return (
      <div style={{ display: "inline-flex", flexDirection: "column" }}>
        <label htmlFor={namePath}>{label}</label>
        {cloneElement(children, this.getControlled(children.props))}
      </div>
    );
  }
}

const Item = (props: ItemProps) => {
  const { name, ...restProps } = props;
  const formContext = useContext(FormContext);
  const namePath = name !== undefined ? getNamePath(name) : undefined;

  return (
    <ItemInternal {...restProps} name={namePath} formContext={formContext} />
  );
};

export default Item;
