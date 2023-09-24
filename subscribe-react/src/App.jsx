import useAppStore from '../hooks/useAppStore';
import './App.css'
import { AppContext, store } from "./stores/createStore";


const Child1 = () => {
  const [count, setState] = useAppStore(state => state.count);

  console.log("Child1");
  return <div>
    <h1 onClick={() => [
      setState(val => ({
        ...val.count,
        count: val.count + 1
      }))
    ]}>Child1</h1>
    <p>{count}</p>
  </div>
}

const Child2 = () => {
  const [num, setState] = useAppStore(state => state.num);

  console.log("Child2");
  return <div>
  <h1 onClick={() => [
      setState({num: num + 1
      })
    ]}>Child2</h1>
  <p>{num}</p>
</div>
}

function App() {
  return (
    <AppContext.Provider value={store}>
      <Child1 />
      <Child2 />
    </AppContext.Provider>
  )
}

export default App
