const Child2 = (props: any) => {
  const { handleAdd } = props;

  return <div onClick={() => handleAdd()}>Child2</div>;
};

export default Child2;
