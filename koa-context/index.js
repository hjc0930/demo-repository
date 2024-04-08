fetch("http://localhost:3000")
  .then(async (res) => {
    res.clone();
    return {
      response: res.clone(),
      data: await res.text(),
    };
  })
  .then((res) => {
    console.log(res);
  });
