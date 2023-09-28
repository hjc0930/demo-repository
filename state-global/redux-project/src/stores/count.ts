import { createSlice } from "@reduxjs/toolkit"

export const counterSlice = createSlice({
  name: 'count',
  initialState: {
    value: 0
  },
  reducers: {
    increment: state => {
      state.value += 1
    }
  }
})
