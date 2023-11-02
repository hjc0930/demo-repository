package main

import "fmt"

func main() {
	type person struct {
		name, city string
		age        int
	}

	p1 := person{
		name: "pprof.cn",
		city: "BeiJing",
		age:  18,
	}

	fmt.Println(p1)
}
