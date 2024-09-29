package utils

import "fmt"

func Hanoi(n int, pillarStart string, pillarMid string, pillarTarget string) {
	if n == 2 {
		fmt.Printf("%s -> %s\n", pillarStart, pillarMid)
		fmt.Printf("%s -> %s\n", pillarStart, pillarTarget)
		fmt.Printf("%s -> %s\n", pillarMid, pillarTarget)
		return
	}
	Hanoi(n-1, pillarStart, pillarTarget, pillarMid)
	fmt.Printf("%s -> %s\n", pillarStart, pillarTarget)
	Hanoi(n-1, pillarMid, pillarStart, pillarTarget)
}
