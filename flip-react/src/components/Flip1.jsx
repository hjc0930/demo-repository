import { useRef, useState } from "react"
import "./flip.css"
import { Flip } from "./flip";

function Flip1() {
  const [list, setList] = useState([
    {
      id:1,
      name: "HTML"
    },
    {
      id:2,
      name: "CSS"
    },
    {
      id:3,
      name: "JavaScript"
    },
    {
      id:4,
      name: "TypeScript"
    },
  ])

  const containerRef = useRef(null);

  const onStart = () => {
    const flip = new Flip(containerRef.current.children)
    const newList = list.toReversed()
    setList([...newList])

    requestAnimationFrame(() => {
      flip.play()
    })
  }

  return (
    <>
      <button onClick={onStart}>Start</button>
      <ul className="container" ref={containerRef}>
        {
          list.map(item => (
            <li key={item.id} className="item">{item.name}</li>
          ))
        }
      </ul>
    </>
  )
}

export default Flip1
