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

//Student 学生
type student struct {
	id   int
	name string
	age  int
}

func main() {
	var b int = 5
	var a int

	numbers := [6]int{1, 2, 3, 4, 5}

	for a := 0; a < 10; a++ {
		fmt.Printf("a is %d", a)
	}

	for a < b {
		a++

		fmt.Printf("a is %d\n", a)
	}

	for i, x := range numbers {
		fmt.Printf("The value of x = %d in the %d place\n", x, i)
	}
}
