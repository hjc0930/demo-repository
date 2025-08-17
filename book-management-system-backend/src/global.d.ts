declare global {
  namespace Express {
    interface Request {
      user?: any; // Define the user property if you are using authentication middleware
    }
  }
}

// eslint-disable-next-line prettier/prettier
export { };
