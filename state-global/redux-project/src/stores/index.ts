import { configureStore } from "@reduxjs/toolkit"
import { counterSlice } from "./count"

export default configureStore({
  reducer: {
    count: counterSlice.reducer,
  }
})