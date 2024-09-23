package main

import "fmt"

func hanoi(n int, pillarStart string, pillarMid string, pillarTarget string) {
	if n == 2 {
		fmt.Printf("%s -> %s\n", pillarStart, pillarMid)
		fmt.Printf("%s -> %s\n", pillarStart, pillarTarget)
		fmt.Printf("%s -> %s\n", pillarMid, pillarTarget)
		return
	}
	hanoi(n-1, pillarStart, pillarTarget, pillarMid)
	fmt.Printf("%s -> %s\n", pillarStart, pillarTarget)
	hanoi(n-1, pillarMid, pillarStart, pillarTarget)
}

func main() {
	hanoi(4, "A", "B", "C")
}
