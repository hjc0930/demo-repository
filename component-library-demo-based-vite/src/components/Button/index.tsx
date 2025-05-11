import classNames from "classnames";

const Button = (props: React.ButtonHTMLAttributes<HTMLButtonElement>) => {
  const { className, children, ...rest } = props;

  return (
    <button {...rest} className={classNames(className, "button")}>
      {children}
    </button>
  );
};

export default Button;
