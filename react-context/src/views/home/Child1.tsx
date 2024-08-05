const Child1 = (props: { handleAdd: () => void }) => {
  const { handleAdd } = props;

  console.log(handleAdd);

  return <div>Child1</div>;
};

export default Child1;
