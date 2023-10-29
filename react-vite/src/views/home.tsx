import useAuth from "./useAuth";

export const Component = () => {
  return "Home";
};

// Init function
export const loader = ({ params }) => {
  console.log(useAuth.role, params);

  return null;
};

export const Meta = {
  async: true,
};
