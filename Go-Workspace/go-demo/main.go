package main

import "fmt"

var arr = []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}

var slice0 []int = arr[2:8]

var slice1 []int = arr[:6]

var slice2 []int = arr[5:]

func main() {
	fmt.Println(arr)
	fmt.Println(slice0)
	fmt.Println(slice1)
	fmt.Println(slice2)
}
