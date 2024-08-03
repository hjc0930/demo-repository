function Form() {
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const { name, email, address } = Object.fromEntries(formData);

    console.log(Object.fromEntries(formData));
  };

  return (
    <form onSubmit={(event) => handleSubmit(event)}>
      <label>First Name:</label>
      <input type="text" name="firstName" />

      <label>Last Name:</label>
      <input type="text" name="lastName" />

      <label>Email:</label>
      <input type="email" name="email" />

      <label>Address:</label>
      <input type="text" name="address" />
      {/* ... 可能会有更多的字段 */}
      <button type="submit">Submit</button>
    </form>
  );
}

export default Form;
