const getQueryparams = (url: string) => {
  if (!url) {
    return {};
  }
  const params = {};
  const searchParams = new URL(url).searchParams;

  for (const [key, value] of searchParams.entries()) {
    params[key] = value;
  }

  return params;
};

export default getQueryparams;
