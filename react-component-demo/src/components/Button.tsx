import React from "react";
import styled from "styled-components";

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement> {}

const StyledButton = styled.button`
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  &:hover {
    background-color: #0056b3;
  }
`;

const Button: React.FC<ButtonProps> = (props) => {
  const { children, ...restProps } = props;
  return <StyledButton {...restProps}>{children}</StyledButton>;
};

export default Button;
