import { useRouterEventEmitterContext } from "../store/routerEventEmitterContext";

const Home = () => {
  const { useSubscription } = useRouterEventEmitterContext();

  useSubscription((event: string, data: any) => {
    console.log("Event received:", event, data);
  });

  return (
    <div>
      <h1>Home</h1>
      <p>Welcome to the Home page!</p>
    </div>
  );
};

export default Home;
