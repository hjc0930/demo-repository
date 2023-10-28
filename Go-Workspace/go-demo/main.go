package main

import "fmt"

// Student 学生
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
	slice := []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}

	d1 := slice[6:8]
	fmt.Println(d1, len(d1), cap(d1))
	d2 := slice[:6:8]
	fmt.Println(d2, len(d2), cap(d2))
}
