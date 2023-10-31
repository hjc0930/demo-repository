package main

import "fmt"

func sum(x int, args ...int) int {
	result := x

	for _, value := range args {
		result += value
	}
	return result
}

func main() {
	result := sum(1, 2, 3, 4)

	fmt.Println(result)
}
