import React from 'react';
import { PropsWithChildren } from 'react';

const Button = (props: PropsWithChildren) => {
  return (
    <button type="button" className="button">
      {props?.children}
    </button>
  );
};

export default Button;
