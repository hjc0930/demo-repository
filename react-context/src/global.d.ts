const app = {
  User: 1,
  Dashboard: 2,
};

declare module "User" {
  const component: ReactElement;
  export default component;
}

declare module Custom {
  interface A {}
}

declare const func: () => Buffer;
